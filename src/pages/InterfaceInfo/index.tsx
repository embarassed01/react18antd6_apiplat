import { getApiInfoById, invokeApiInfo, listApiInfo, listApiInfoByPage } from '@/services/api-backend/apiinfoController';
import { PageContainer } from '@ant-design/pro-components';
import { useMatch, useParams } from '@umijs/max';
import { Button, Card, Descriptions, Form, List, message, Skeleton, Spin } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 * @returns 
 */
const Index: React.FC = () => {
  // 2个状态变量
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.Apiinfo>();
  // const match = useMatch('/interface_info/:id');  // 拿的更全，这里直接用useParams拿url参数即可
  const params = useParams();
  const [invokeRes, setInvokeRes] = useState<any>();
  const [invokeLoading, setInvokeLoading] = useState(false);

  const loadData = async () => {
    if (!params.id) {
        message.error('参数不存在');
        return;
    }
    setLoading(true);
    try {
      const res = await getApiInfoById({
        id: Number(params.id)
    });
      setData(res.data);
    } catch (error: any) {
      message.error('请求失败, ' + error.message);
    }
    setLoading(false);  // 表示已经加载完
  };

  // 加载数据的逻辑
  useEffect(() => {
    loadData();
  }, []);

  const onFinish = async (values: any) => {
    if (!params.id) {
        message.error("接口不存在");
        return;
    }
    setInvokeLoading(true);
    try {
        const res = await invokeApiInfo({
            id: params.id,
            ...values,
        });
        setInvokeRes(res.data);
        message.success('请求成功');
    } catch (error: any) {
        message.error('操作失败, ' + error.message);
    }
    setInvokeLoading(false);
  };

  return (
    <PageContainer title='查看接口文档'>
        <Card>
            {
            data ? 
            (
            <Descriptions title={data.name} column={1} extra={<Button>假调用</Button>} >
                <Descriptions.Item label="接口状态">{data.status ? '开启' : '关闭'}</Descriptions.Item>
                <Descriptions.Item label="描述">{data.description}</Descriptions.Item>
                <Descriptions.Item label="请求地址">{data.url}</Descriptions.Item>
                <Descriptions.Item label="请求方法">{data.method}</Descriptions.Item>
                <Descriptions.Item label="请求参数">{data.requestParams}</Descriptions.Item>
                <Descriptions.Item label="请求头">{data.requestHeader}</Descriptions.Item>
                <Descriptions.Item label="响应头">{data.responseHeader}</Descriptions.Item>
                <Descriptions.Item label="创建时间">{data.createTime}</Descriptions.Item>
                <Descriptions.Item label="更新时间">{data.updateTime}</Descriptions.Item>

            </Descriptions>
            ) : 
            (
            <>接口不存在</>
            )}
        </Card>
        <Card title='在线测试'>
            <Form
                name="invoke"
                layout='vertical'
                onFinish={onFinish}
            >
                <Form.Item
                    label="请求参数"
                    name="requestParams"
                >
                    <TextArea />
                </Form.Item>
                <Form.Item wrapperCol={{ span: 16 }}>
                    <Button type='primary' htmlType='submit'>
                        调用(测试)
                    </Button>
                </Form.Item>
            </Form>
        </Card>
        <Card title="返回结果" loading={invokeLoading}>
            {invokeRes}
        </Card>
    </PageContainer>
  );
};

export default Index;
