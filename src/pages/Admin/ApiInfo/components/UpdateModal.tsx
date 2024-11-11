import {
  ProColumns,
  ProFormInstance,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React, { useEffect, useRef } from 'react';
export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;

export type Props = {
  values: API.Apiinfo;
  columns: ProColumns<API.Apiinfo>[];
  onCancel: () => void;
  onSubmit: (values: API.Apiinfo) => Promise<void>;
  visible: boolean;  // 该模态框是否可见
};

const UpdateModal: React.FC<Props> = (props) => {
  const {columns, visible, onCancel, onSubmit, values} = props;

  // 创建一个ref引用：
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(values);
    }
  }, [values]);

  return (
    <Modal visible={visible} footer={null} onCancel={() => onCancel?.()} > 
      <ProTable 
      type="form" 
      columns={columns} 
      // 注意：不要使用如下的form，因为只能修改一次，要改成formRef
      // form={{
      //   initialValues: values,  // 只能填充一次，之后就改不了了!!所以要改成监听外部values的变化 `useEffect`
      // }}
      formRef={formRef}
      onSubmit={async (values) => {
          onSubmit?.(values);
        }}
      />
    </Modal>
  );
};
export default UpdateModal;
