import { AvatarDropdown, AvatarName, Footer, Question } from '@/components';
import type {Settings as LayoutSettings } from '@ant-design/pro-layout';
import { LinkOutlined } from '@ant-design/icons';
import { SettingDrawer } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history, Link } from '@umijs/max';
import { requestConfig } from './requestConfig';
import { getLoginUser } from './services/api-backend/userController';
import { defaultSettings } from '@ant-design/pro-layout/es/defaultSettings';
// import InitialState from './.umi/plugin-initialState/@@initialState';
const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * 相当于 全局变量，保存 用户登录态信息 状态
 * */
export async function getInitialState(): Promise<InitialState> {
  // 当页面首次加载时，获取要全局保存的数据，比如：用户登录信息
  const initialState: InitialState = {
    currentUser: undefined,
  };
  // 如果不是登录页面，执行
  const {location} = history;
  if (location.pathname !== loginPath) {
    try {
      const res = await getLoginUser();
      initialState.currentUser = res.data;
    } catch (error: any) {
      // 如果未登录
    }
  }
  return initialState;
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    avatarProps: {
      render: () => {
        return <AvatarDropdown />;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.userName,
    },
    footerRender: () => <Footer />,

    // 注意：需要删除，否则会重复进入登录页，需要登录两次才能正常进入主页！！
    // onPageChange: () => {
    //   const { location } = history;
    //   // 如果没有登录，重定向到 login
    //   if (!initialState?.currentUser && location.pathname !== loginPath) {
    //     history.push(loginPath);
    //   }
    // },
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    ...defaultSettings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
// export const request = {
//   ...requestConfig,
// };
export const request = requestConfig;
