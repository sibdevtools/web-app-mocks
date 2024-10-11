import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Alert, Button, Table } from 'react-bootstrap';
import {
  MockInvocation, getInvocation, MockInvocationDefaults
} from '../../api/service';
import { Loader } from '../../components/Loader';
import { useNavigate, useParams } from 'react-router-dom';
import { contextPath } from '../../const/common.const';
import { ArrowLeft01Icon } from 'hugeicons-react';

const MockInvocationPage: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [invocation, setInvocation] = useState<MockInvocation>(MockInvocationDefaults);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();
  const { serviceId, mockId, invocationId } = useParams();

  useEffect(() => {
    if (mockId) {
      fetchInvocation();
    } else {
      setLoading(false);
    }
  }, [mockId]);

  const fetchInvocation = async () => {
    if (!(serviceId && mockId && invocationId)) {
      setLoading(false);
      return;
    }
    setLoading(true);
    try {
      const response = await getInvocation(+serviceId, +mockId, +invocationId);
      if (response.data.success) {
        setInvocation(response.data.body);
      } else {
        setError('Failed to fetch invocation');
        return;
      }
    } catch (error) {
      console.error('Failed to fetch invocation:', error);
      setError('Failed to fetch invocation');
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    navigate(`${contextPath}service/${serviceId}/mocks/invocations/${mockId}`);
  };

  // Helper function to decode Base64 body
  const decodeBase64 = (data: string | null) => {
    return data ? atob(data) : null;
  };

  // Helper function to safely convert Map to object or return null
  const convertMapToObject = (map: Map<string, Array<string> | null> | null) => {
    if (map) {
      return Object.fromEntries(map.entries());
    }
    return null;
  };

  return (
    <Container className="mt-4 mb-4">
      <Row>
        <Col md={{ span: 1, offset: 2 }} className={'mb-2'}>
          <Button variant={'outline-primary'} type={'button'} onClick={handleBack}>
            <ArrowLeft01Icon />
          </Button>
        </Col>
        <Col md={{ span: 8 }}>
          <span className={'h2'}>HTTP Mock Invocation</span>
        </Col>
      </Row>
      <Row>
        <Col md={12}>
          <Container className="mt-4">
            {loading ? (
              <Loader />
            ) : (
              <>
                {error && (
                  <Alert variant="danger" onClose={() => setError(null)} dismissible>
                    {error}
                  </Alert>
                )}

                {!error && (
                  <Table striped bordered hover>
                    <tbody>
                    <tr>
                      <td><strong>Remote Client Host</strong></td>
                      <td>{invocation.remoteHost || 'N/A'}</td>
                    </tr>
                    <tr>
                      <td><strong>Remote Client Address</strong></td>
                      <td>{invocation.remoteAddress || 'N/A'}</td>
                    </tr>
                    <tr>
                      <td><strong>HTTP Method</strong></td>
                      <td>{invocation.method}</td>
                    </tr>
                    <tr>
                      <td><strong>Request Path</strong></td>
                      <td>{invocation.path}</td>
                    </tr>
                    <tr>
                      <td><strong>Query Params</strong></td>
                      <td>
                        {invocation.queryParams ? (
                          <pre>{JSON.stringify(convertMapToObject(invocation.queryParams), null, 2)}</pre>
                        ) : 'N/A'}
                      </td>
                    </tr>
                    <tr>
                      <td><strong>Execution Timing</strong></td>
                      <td>{invocation.timing} ms</td>
                    </tr>
                    <tr>
                      <td><strong>Status Code</strong></td>
                      <td>{invocation.status}</td>
                    </tr>
                    <tr>
                      <td><strong>Invocation Date Time</strong></td>
                      <td>{invocation.createdAt}</td>
                    </tr>
                    <tr>
                      <td><strong>Request Body</strong></td>
                      <td><pre>{decodeBase64(invocation.rqBody)}</pre></td>
                    </tr>
                    <tr>
                      <td><strong>Request Headers</strong></td>
                      <td>
                        {invocation.rqHeaders ? (
                          <pre>{JSON.stringify(convertMapToObject(invocation.rqHeaders), null, 2)}</pre>
                        ) : 'N/A'}
                      </td>
                    </tr>
                    <tr>
                      <td><strong>Response Body</strong></td>
                      <td><pre>{decodeBase64(invocation.rsBody)}</pre></td>
                    </tr>
                    <tr>
                      <td><strong>Response Headers</strong></td>
                      <td>
                        {invocation.rsHeaders ? (
                          <pre>{JSON.stringify(convertMapToObject(invocation.rsHeaders), null, 2)}</pre>
                        ) : 'N/A'}
                      </td>
                    </tr>
                    </tbody>
                  </Table>
                )}
              </>
            )}
          </Container>
        </Col>
      </Row>
    </Container>
  );
};

export default MockInvocationPage;
