import React from 'react';
import { Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';

const DynamicBreadcrumb = ({ items }) => {
  return (
    <div className="bg-light mx-0 my-1 px-0 pt-3 pb-1 rounded">
      <Breadcrumb className="mb-3">
        <BreadcrumbItem>
          <Link to="/" className="text-decoration-none text-secondary">
            <FontAwesomeIcon icon={faHome} className="me-1" />
          </Link>
        </BreadcrumbItem>
        {items.map((item, index) => (
          <BreadcrumbItem
            key={index}
            active={index === items.length - 1}
          >
            {index === items.length - 1 ? (
              <span className="fw-semibold text-dark">
                {item.label}
              </span>
            ) : (
              <Link
                to={item.url}
                className="text-decoration-none text-secondary"
              >
                {item.label}
              </Link>
            )}
          </BreadcrumbItem>
        ))}
      </Breadcrumb>
    </div>
  );
};

export default DynamicBreadcrumb;