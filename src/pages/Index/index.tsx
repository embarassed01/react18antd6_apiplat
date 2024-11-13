import { listApiInfo, listApiInfoByPage } from '@/services/api-backend/apiinfoController';
import { PageContainer } from '@ant-design/pro-components';
import { List, message, Skeleton } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 * @returns 
 */
const Index: React.FC = () => {
  // 三个状态变量
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.Apiinfo[]>([]);
  const [total, setTotal] = useState<number>(0);

  const loadData = async (current: number = 1, pageSize: number = 5) => {
    setLoading(true);
    try {
      const res = await listApiInfoByPage({
        current, pageSize,
      });
      setList(res.data?.records ?? []);  // 可选参数可能没有值，所以给个默认值兜底
      setTotal(res.data?.total ?? 0);
    } catch (error: any) {
      message.error('请求失败, ' + error.message);
    }
    setLoading(false);  // 表示已经加载完
  };

  // 加载数据的逻辑
  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title='在线接口开放平台'>
      <List 
        className='my-list'
        loading={loading}
        itemLayout='horizontal'
        dataSource={list}
        renderItem={(item) => {
          const apiLink = `/interface_info/${item.id}`;
          // key保证每条数据不重复！！
          return (
            <List.Item
              actions={[<a key={item.id} href={apiLink}>查看</a>]}
            >
              <List.Item.Meta 
                title={<a href={apiLink}>{item.name}</a>}
                description={item.description}
              />
            </List.Item>
          );
        }}
        pagination={
          {
            showTotal(tot: number) {
              return '总数: ' + tot;
            },
            pageSize: 5,
            total: total,
            onChange(page, pageSize) {
              loadData(page, pageSize);
            },
          }
        }
      />
    </PageContainer>
  );
};

export default Index;
