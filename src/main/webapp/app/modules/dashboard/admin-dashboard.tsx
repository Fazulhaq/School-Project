import React from "react";
import { Row, Col, Card, CardBody, CardTitle, CardText, Container } from "reactstrap";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend } from "recharts";

// Sample data for graph
const sampleData = [
  { name: "Math", Students: 40, Teachers: 5 },
  { name: "Science", Students: 35, Teachers: 4 },
  { name: "English", Students: 50, Teachers: 6 },
  { name: "History", Students: 30, Teachers: 3 },
];

const AdminDashboard = () => {
  return (
    <Container className="my-5">
      {/* Dashboard Header */}
      <h1 className="mb-4 text-primary fw-bold text-center">Admin Dashboard</h1>

      {/* Cards Section */}
      <Row className="mb-4">
        <Col md="3" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Total Students</CardTitle>
              <CardText className="display-6 text-success">350</CardText>
            </CardBody>
          </Card>
        </Col>

        <Col md="3" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Total Teachers</CardTitle>
              <CardText className="display-6 text-info">25</CardText>
            </CardBody>
          </Card>
        </Col>

        <Col md="3" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Active Classes</CardTitle>
              <CardText className="display-6 text-warning">12</CardText>
            </CardBody>
          </Card>
        </Col>

        <Col md="3" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Today Attendance</CardTitle>
              <CardText className="display-6 text-danger">320</CardText>
            </CardBody>
          </Card>
        </Col>
      </Row>

      {/* Graph Section */}
      <Row>
        <Col md="12">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody>
              <CardTitle tag="h5" className="fw-bold mb-3">Students vs Teachers by Subject</CardTitle>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={sampleData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="Students" fill="#0d6efd" />
                  <Bar dataKey="Teachers" fill="#198754" />
                </BarChart>
              </ResponsiveContainer>
            </CardBody>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default AdminDashboard;