import React from 'react';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { Link } from 'react-router-dom';
import '@fortawesome/fontawesome-free/css/all.min.css'; // Ensure Font Awesome is loaded
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';

const DynamicBreadcrumb = ({ items }) => {
  return (
    <Breadcrumb tag="nav" listTag="div">
      {/* Home breadcrumb with Font Awesome icon */}
      <BreadcrumbItem>
        <Link to="/" style={{ textDecoration: 'none', color: 'inherit' }}>
          <FontAwesomeIcon icon={faHome} className="me-2" />
        </Link>
      </BreadcrumbItem>

      {/* Dynamic items */}
      {items.map((item, index) => (
        <BreadcrumbItem
          key={index}
          active={index === items.length - 1} // Last item is active
        >
          {index === items.length - 1 ? (
            <span>{item.label}</span> // Active item, no link
          ) : (
            <Link to={item.url} style={{ textDecoration: 'none', color: 'inherit' }}>
              {item.label}
            </Link>
          )}
        </BreadcrumbItem>
      ))}
    </Breadcrumb>
  );
};

export default DynamicBreadcrumb;