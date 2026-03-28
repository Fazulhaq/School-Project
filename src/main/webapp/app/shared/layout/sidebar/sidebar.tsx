import React from 'react';
import { NavLink } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppSelector } from 'app/config/store';
import { entities } from 'app/config/entities';
import './sidebar.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const Sidebar = () => {
  const account = useAppSelector(state => state.authentication.account);
  const userRoles = account?.authorities || [];
  const isAdmin = userRoles.includes('ROLE_ADMIN');
  const isUser = userRoles.includes('ROLE_USER');

  const hasAccess = entity => {
    if (isAdmin) return entity.roles.includes('ROLE_ADMIN');
    if (isUser) return entity.roles.includes('ROLE_USER');
    return false;
  };

  return (
    <div className="sidebar">
      <h5 className="sidebar-title text-center">KSMIS</h5>
      <ul className="sidebar-menu">
        <li>
          <NavLink to="/" className="sidebar-link">
            Dashboard
          </NavLink>
        </li>
        {entities
          .filter(hasAccess)
          .map((entity, index) => (
            <li key={index}>
              <NavLink to={entity.path} className="sidebar-link">
                <FontAwesomeIcon icon={entity.icon} className="me-2" />
                <Translate contentKey={`global.menu.entities.${entity.name}`} />
              </NavLink>
            </li>
          ))}
      </ul>
    </div>
  );
};

export default Sidebar;