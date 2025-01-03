import { removeRule } from '@/services/ant-design-pro/api';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  ModalForm,
  PageContainer,
  ProDescriptions,
  ProFormText,
  ProFormTextArea,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Button, Drawer, message } from 'antd';
import React, { useRef, useState } from 'react';
import { addApiInfo, deleteApiInfo, listApiInfoByPage, offlineApiInfo, onlineApiInfo, updateApiInfo } from '@/services/api-backend/apiinfoController';
import CreateModal from './components/CreateModal';
import UpdateModal from './components/UpdateModal';


const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();  // actionRef能够拿到ProTable组件的控制权！！
  const [currentRow, setCurrentRow] = useState<API.Apiinfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.RuleListItem[]>([]);
  // const [currentId, setCurrentId] = useState<number>();

  /**
   * @en-US Add node
   * @zh-CN 添加节点
   * @param fields
   */
  const handleAdd = async (fields: API.Apiinfo) => {
    const hide = message.loading('正在添加');
    try {
      await addApiInfo({
        ...fields,
      });
      hide();
      message.success('创建成功');
      // 关闭模态框
      handleModalOpen(false);
      return true;
    } catch (error: any) {
      hide();
      console.log(error);
      message.error('创建失败, ' + error.message);
      return false;
    }
  };

  /**
   * @en-US Update node
   * @zh-CN 更新节点
   *
   * @param fields
   */
  const handleUpdate = async (fields: API.Apiinfo) => {
    if (!currentRow) {
      return;
    }
    const hide = message.loading('修改中');
    try {
      await updateApiInfo({
        id: currentRow.id,
        ...fields,
      });
      hide();
      message.success('操作成功');
      // 关闭模态框
      handleUpdateModalOpen(false);
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败, ' + error.message);
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param record
   */
  const handleRemove = async (record: API.Apiinfo) => {
    const hide = message.loading('正在删除');
    if (!record) return true;
    try {
      await deleteApiInfo({
        id: record.id,
      });
      hide();
      message.success('删除成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败, ' + error.message);
      return false;
    }
  };

  /**
   * 接口发布
   * @param record 
   * @returns 
   */
  const handleOnline = async (record: API.IdRequest) => {
    const hide = message.loading('发布中');
    if (!record) return true;
    try {
      await onlineApiInfo({
        id: record.id,
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败, ' + error.message);
      return false;
    }
  };

  /**
   * 接口下线
   * @param record 
   * @returns 
   */
  const handleOffline = async (record: API.IdRequest) => {
    const hide = message.loading('下线中');
    if (!record) return true;
    try {
      await offlineApiInfo({
        id: record.id,
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败, ' + error.message);
      return false;
    }
  };

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */

  const columns: ProColumns<API.Apiinfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'index',  // index意味着：在传递的时候会屏蔽掉，因为是表格的行序号，不是数据库表的id项
      // valueType: 'number',
    },
    {
      title: '接口名称',
      dataIndex: 'name',
      valueType: 'text',
      formItemProps: {
        rules: [{
          required: true,
          //message: '接口名称必须填写',  // default: '请输入接口名称'
        }],
      },
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
    },
    {
      title: '请求方法',
      dataIndex: 'method',
      valueType: 'text',
      formItemProps: {
        rules: [{
          required: true,
        }]
      }
    },
    {
      title: 'url',
      dataIndex: 'url',
      valueType: 'text',
      formItemProps: {
        rules: [{
          required: true,
        }]
      }
    },
    {
      title: '请求参数',
      dataIndex: 'requestParams',
      valueType: 'jsonCode',
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'jsonCode',
      formItemProps: {
        rules: [{
          required: true,
        }]
      }
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'jsonCode',  // textarea
      formItemProps: {
        rules: [{
          required: true,
        }]
      }
    },
    {
      title: '状态',
      dataIndex: 'status',
      hideInForm: true,
      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '开启',
          status: 'Processing',
        },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="config"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
        >
          修改
        </a>,

        record.status === 0 ? <a
        key="config"
        onClick={() => {
          handleOnline(record);
        }}
        >
         发布
        </a> : 
        <Button
          type='text'
          danger
          key="config"
          onClick={() => {
            handleOffline(record);
          }}
        >
          下线
        </Button>,

        <Button
        type="text"
        danger
        key="config"
        onClick={() => {
          handleRemove(record);
        }}
        >
          删除
        </Button>,
      ],
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.RuleListItem, API.PageParams>
        headerTitle={'查询表格'}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        // 修改请求：根据request方法的参数和返回值类型，将我们的调用api加入其中！！
        // @ts-ignore
        request={
          async (params, sort, filter) => {
            const sortField = Object.keys(sort)?.[0];
            const sortOrder = sort?.[sortField] ?? undefined;
            // @ts-ignore
            const {data, code} = await listApiInfoByPage({
              ...params,
              sortField,
              sortOrder,
              ...filter,
            } as API.ApiinfoQueryRequest);
            return {
              success: code === 0,
              data: data?.records || [],
              total: Number(data?.total) || 0,
            };
          }
        //   async (params, sort: Record<string, SortOrder>, filter: Record<string, (string | number)[] | null>) => {
        //   // @ts-ignore
        //   const res = await listApiInfoByPage({
        //     ...params,
        //   });
        //   if (res?.data) {
        //     return {
        //       data: res?.data.records || [],
        //       success: true,
        //       total: res?.data.total,
        //     }
        //   }
        // }
        }
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              已选择{' '}
              <a
                style={{
                  fontWeight: 600,
                }}
              >
                {selectedRowsState.length}
              </a>{' '}
              项 &nbsp;&nbsp;
              <span>
                服务调用次数总计 {selectedRowsState.reduce((pre, item) => pre + item.callNo!, 0)} 万
              </span>
            </div>
          }
        >
          <Button
            onClick={async () => {
              await handleRemove(selectedRowsState);
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            批量删除
          </Button>
          <Button type="primary">批量审批</Button>
        </FooterToolbar>
      )}
      <ModalForm
        title={'新建规则'}
        width="400px"
        open={createModalOpen}
        onOpenChange={handleModalOpen}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.RuleListItem);
          if (success) {
            handleModalOpen(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
          rules={[
            {
              required: true,
              message: '规则名称为必填项',
            },
          ]}
          width="md"
          name="name"
        />
        <ProFormTextArea width="md" name="desc" />
      </ModalForm>
      <UpdateModal
        columns={columns}
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        visible={updateModalOpen}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
      <CreateModal columns={columns} onCancel={() => {handleModalOpen(false)}} onSubmit={(values) => {handleAdd(values)}} visible={createModalOpen} />
    </PageContainer>
  );
};
export default TableList;
