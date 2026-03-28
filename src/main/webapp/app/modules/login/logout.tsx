import React, { useEffect } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';

export const Logout = () => {
  const authentication = useAppSelector(state => state.authentication);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(logout());

    const timeoutId = setTimeout(() => {
      if (authentication.logoutUrl) {
        window.location.href = authentication.logoutUrl;
      } else {
        window.location.href = '/';
      }
    }, 100);

    return () => clearTimeout(timeoutId);
  }, [dispatch, authentication.logoutUrl]);

   return null;
};

export default Logout;
