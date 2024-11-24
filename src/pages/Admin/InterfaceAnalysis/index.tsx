import type { ActionType } from '@ant-design/pro-components';
import {
  PageContainer,
} from '@ant-design/pro-components';
import '@umijs/max';
import React, { useEffect, useRef, useState } from 'react';
import ReactECharts from 'echarts-for-react';
import { listTopInvokeInterfaceInfo } from '@/services/api-backend/analysisController';

/**
 * 接口分析
 * @returns
 */
const InterfaceAnalysis: React.FC = () => {
  
  const [data, setData] = useState<API.InterfaceInfoVO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    try {
      listTopInvokeInterfaceInfo().then(res => {
        if (res.data) {
          setData(res.data);
        }
      })
    } catch (e: any) {
    }
    // TODO 从远程获取数据
  }, []);

  // 映射：{ value: 1048, name: 'Search Engine' },
  const chartData = data.map(item => {
    return {
      value: item.totalNum,
      name: item.name,
    }
  });

  const option = {
    title: {
      text: '调用次数最多的接口TOP3',
      // subtext: 'Fake Data',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: chartData,
        // data: [
        //   { value: 1048, name: 'Search Engine' },
        //   { value: 735, name: 'Direct' },
        //   { value: 580, name: 'Email' },
        //   { value: 484, name: 'Union Ads' },
        //   { value: 300, name: 'Video Ads' }
        // ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };  

  return (
    <PageContainer>
      <ReactECharts loadingOption={{
        showLoading: loading
      }} option={option} />
    </PageContainer>
  );
};

export default InterfaceAnalysis;
