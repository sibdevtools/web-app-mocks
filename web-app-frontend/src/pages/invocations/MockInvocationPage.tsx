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

  const decodeBase64 = (data: string | null) => {
    return data ? atob(data) : null;
  };

  const renderTableRows = (map: Map<string, Array<string> | null> | null) => {
    if (!map || !(map instanceof Map)) return <tr><td colSpan={2}>N/A</td></tr>;

    const entries = Array.from(map.entries());
    if (entries.length === 0) return <tr><td colSpan={2}>N/A</td></tr>;

    return entries.map(([key, value], idx) => (
      <tr key={idx}>
        <td>{key}</td>
        <td>{value ? value.join(', ') : 'N/A'}</td>
      </tr>
    ));
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
                  <>
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
                        <td><strong>Response Body</strong></td>
                        <td><pre>{decodeBase64(invocation.rsBody)}</pre></td>
                      </tr>
                      </tbody>
                    </Table>

                    <h4 className="mt-4">Query Params</h4>
                    <Table striped bordered hover>
                      <thead>
                      <tr>
                        <th>Key</th>
                        <th>Value</th>
                      </tr>
                      </thead>
                      <tbody>
                      {renderTableRows(invocation.queryParams)}
                      </tbody>
                    </Table>

                    <h4 className="mt-4">Request Headers</h4>
                    <Table striped bordered hover>
                      <thead>
                      <tr>
                        <th>Key</th>
                        <th>Value</th>
                      </tr>
                      </thead>
                      <tbody>
                      {renderTableRows(invocation.rqHeaders)}
                      </tbody>
                    </Table>

                    <h4 className="mt-4">Response Headers</h4>
                    <Table striped bordered hover>
                      <thead>
                      <tr>
                        <th>Key</th>
                        <th>Value</th>
                      </tr>
                      </thead>
                      <tbody>
                      {renderTableRows(invocation.rsHeaders)}
                      </tbody>
                    </Table>
                  </>
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
