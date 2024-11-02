import {
  ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';
export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;

export type Props = {
  columns: ProColumns<API.Apiinfo>[];
  onCancel: () => void;
  onSubmit: (values: API.Apiinfo) => Promise<void>;
  visible: boolean;  // 该模态框是否可见
};

const CreateModal: React.FC<Props> = (props) => {
  const {columns, visible, onCancel, onSubmit} = props;
  return (
    <Modal visible={visible} footer={null} onCancel={() => onCancel?.()} > 
      {/* 把protable封装到modal里 */}
      <ProTable type="form" columns={columns} onSubmit={async (values) => {
          onSubmit?.(values);
        }}
      />
    </Modal>
  );
};
export default CreateModal;
