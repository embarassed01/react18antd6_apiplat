/**
 * @see https://umijs.org/docs/max/access#access
 * 
 * 权限管理
 * */
export default function access(initialState: InitialState | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canUser: currentUser,
    canAdmin: currentUser?.userRole === 'admin',
  };
}
