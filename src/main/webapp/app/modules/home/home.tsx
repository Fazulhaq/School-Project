/* eslint-disable react/no-unescaped-entities */
import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Button, Container } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import AdminDashboard from 'app/modules/dashboard/admin-dashboard';
import UserDashboard from 'app/modules/dashboard/user-dashboard';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const roles = account?.authorities || [];
  const isAdmin = roles.includes('ROLE_ADMIN');
  const isUser = roles.includes('ROLE_USER');

  // Render the dashboard if logged in
  if (account?.login) {
    if (isAdmin) {
      return <AdminDashboard />;
    } else if (isUser) {
      return <UserDashboard />;
    } else {
      return <div>No dashboard available for your role</div>;
    }
  }

  // Render landing page if not logged in
  return (
    <Container className="my-5 py-5 justify-content-center">
      <Row className="justify-content-center text-center">
        <Col md="9">
          <div className="p-5 bg-light rounded-4 shadow-sm">
            <h1 className="display-4 fw-bold text-primary mb-3">
              Karimi School Management Information System
            </h1>
            <p className="lead text-muted mb-4">
              A modern School Management Information System to manage students,
              teachers, classes, and operations efficiently.
            </p>

            <div className="mb-4">
              <Link to="/login">
                <Button
                  color="primary"
                  size="lg"
                  className="px-5 py-2 fw-semibold shadow"
                  style={{ borderRadius: '30px' }}
                >
                  Sign In
                </Button>
              </Link>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default Home;