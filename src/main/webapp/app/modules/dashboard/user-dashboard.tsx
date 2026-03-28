import React from "react";
import { Row, Col, Card, CardBody, CardTitle, CardText, Container } from "reactstrap";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend } from "recharts";

// Sample data for chart
const attendanceData = [
  { day: "Mon", Present: 30, Absent: 5 },
  { day: "Tue", Present: 28, Absent: 7 },
  { day: "Wed", Present: 32, Absent: 3 },
  { day: "Thu", Present: 29, Absent: 6 },
  { day: "Fri", Present: 31, Absent: 4 },
];

const UserDashboard = () => {
  return (
    <Container className="my-5">
      {/* Dashboard Header */}
      <h1 className="mb-4 text-primary fw-bold text-center">User Dashboard</h1>

      {/* Cards Section */}
      <Row className="mb-4">
        <Col md="4" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Your Classes</CardTitle>
              <CardText className="display-6 text-info">5</CardText>
            </CardBody>
          </Card>
        </Col>

        <Col md="4" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Subjects</CardTitle>
              <CardText className="display-6 text-success">6</CardText>
            </CardBody>
          </Card>
        </Col>

        <Col md="4" sm="6" className="mb-3">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody className="text-center">
              <CardTitle tag="h5" className="fw-bold">Pending Assignments</CardTitle>
              <CardText className="display-6 text-warning">2</CardText>
            </CardBody>
          </Card>
        </Col>
      </Row>

      {/* Graph Section */}
      <Row>
        <Col md="12">
          <Card className="shadow-sm border-0 rounded-4">
            <CardBody>
              <CardTitle tag="h5" className="fw-bold mb-3">Attendance This Week</CardTitle>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={attendanceData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                  <XAxis dataKey="day" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="Present" fill="#198754" />
                  <Bar dataKey="Absent" fill="#dc3545" />
                </BarChart>
              </ResponsiveContainer>
            </CardBody>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default UserDashboard;