import React from 'react';
import Sidebar from 'app/shared/layout/sidebar/sidebar';
import { useAppSelector } from 'app/config/store';

const Layout = ({ children }) => {

    const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

    return (
        <div className={`app-container ${isAuthenticated ? 'with-sidebar' : 'no-sidebar'}`}>
            {isAuthenticated && <Sidebar />}
            <div className="main-content">
                {children}
            </div>
        </div>
    );
};

export default Layout;