import React from 'react';
import { Button, Col, Container, Form, InputGroup, OverlayTrigger, Row, Table, Tooltip } from 'react-bootstrap';
import { methods, MockType, mockTypes, StatusCode, statusCodes } from '../../const/common.const';
import HttpHeadersForm from '../../components/HttpHeadersForm';
import StaticMockContent from '../../components/StaticMockContent';
import GraalVMMockContent from '../../components/GraalVMMockContent';
import StaticFileMockContent from '../../components/StaticFileMockContent';
import { ArrowLeft01Icon, FloppyDiskIcon } from 'hugeicons-react';
import { Loader } from '../../components/Loader';
import './MockForm.css';
import CodeDocumentation from './CodeDocumentation';
import SuggestiveInput from '../../components/suggestive-input/SuggestiveInput';

type MockFormProps = {
  loading: boolean;
  mockName: string;
  method: string;
  path: string;
  delay: number;
  mockType: MockType;
  meta: { [key: string]: string };
  content: ArrayBuffer;
  setMockName: (name: string) => void;
  setMethod: (method: string) => void;
  setPath: (path: string) => void;
  setDelay: (delay: number) => void;
  setMockType: (type: MockType) => void;
  setMeta: (meta: { [key: string]: string }) => void;
  setContent: (content: ArrayBuffer) => void;
  onSubmit: (e: React.FormEvent) => void;
  isEditMode: boolean;
  navigateBack: () => void;
};

export const MockForm: React.FC<MockFormProps> = ({
                                                    loading,
                                                    mockName,
                                                    method,
                                                    path,
                                                    delay,
                                                    mockType,
                                                    meta,
                                                    content,
                                                    setMockName,
                                                    setMethod,
                                                    setPath,
                                                    setDelay,
                                                    setMockType,
                                                    setMeta,
                                                    setContent,
                                                    onSubmit,
                                                    isEditMode,
                                                    navigateBack
                                                  }) => {

  const onStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newStatusCode = +e.target.value as StatusCode;
    if (newStatusCode) {
      setMeta({ ...meta, STATUS_CODE: `${newStatusCode}` });
    }
  };

  const onMockTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setMockType(e.target.value as MockType);
  };

  const pathTooltip = (
    <Tooltip id="pathTooltip" className={'wide-tooltip'}>
      <Table variant={'dark'} borderless={true} responsive={true}>
        <thead>
        <tr>
          <th>Wildcard</th>
          <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td><code>?</code></td>
          <td>Matches exactly one character.</td>
        </tr>
        <tr>
          <td><code>*</code></td>
          <td>Matches zero or more characters.</td>
        </tr>
        <tr>
          <td><code>**</code></td>
          <td>Matches zero or more 'directories' in a path</td>
        </tr>
        <tr>
          <td><code>&#123;spring:[a-z]+&#125;</code></td>
          <td>Matches regExp <code>[a-z]+</code> as a path variable named <code>"spring"</code></td>
        </tr>
        </tbody>
      </Table>
    </Tooltip>
  );

  return (
    <Container className="mt-4 mb-4">
      <Row className="mb-2">
        <Col md={{ span: 1, offset: 2 }}>
          <Button
            variant="outline-primary"
            onClick={navigateBack}
            title={'Back'}
          >
            <ArrowLeft01Icon />
          </Button>
        </Col>
        <Col md={9}>
          <h2>{isEditMode ? 'Edit Mock' : 'Add Mock'}</h2>
        </Col>
      </Row>
      {loading ?
        <Loader />
        :
        <Row>
          <Col md={{ span: 10, offset: 1 }}>
            <Form className="mt-4" onSubmit={onSubmit}>
              {/* Mock Name Input */}
              <Form.Group className="mb-3" controlId="mockNameInput">
                <Row>
                  <Col lg={2}>
                    <Form.Label htmlFor={'nameInput'}>Name</Form.Label>
                  </Col>
                  <Col lg={10}>
                    <Form.Control
                      type="text"
                      id={'nameInput'}
                      value={mockName}
                      onChange={(e) => setMockName(e.target.value)}
                      required
                    />
                  </Col>
                </Row>
              </Form.Group>

              {/* Method and Path Fields */}
              <Row className="mb-3">
                <Col md={2} sm={3}>
                  <Form.Group controlId="httpMethodSelect">
                    <Form.Label>HTTP Method</Form.Label>
                    <SuggestiveInput
                      mode={'free'}
                      value={method}
                      onChange={it => setMethod(it.value)}
                      required={true}
                      suggestions={methods.map(it => {
                        return { key: it, value: it };
                      })}
                      maxSuggestions={methods.length}
                    />
                  </Form.Group>
                </Col>

                <Col md={10} sm={9}>
                  <Form.Group controlId="pathInput">
                    <Form.Label htmlFor={'pathInput'}>Path</Form.Label>
                    <InputGroup>
                      <InputGroup.Text>/</InputGroup.Text>
                      <OverlayTrigger
                        placement="bottom"
                        overlay={pathTooltip}
                        delay={{ show: 25, hide: 250 }}
                      >
                        <Form.Control
                          type="text"
                          id={'pathInput'}
                          value={path}
                          onChange={(e) => setPath(e.target.value)}
                          placeholder="Ant pattern or path"
                          required
                        />
                      </OverlayTrigger>
                    </InputGroup>
                  </Form.Group>
                </Col>
              </Row>

              {/* HTTP Headers */}
              <Form.Group className="mb-3">
                <Form.Label>Http Headers</Form.Label>
                <HttpHeadersForm meta={meta} setMeta={setMeta} />
              </Form.Group>

              {/* Status and Mock Type Fields */}
              <Row className="mb-3">
                <Col md={8}>
                  <Form.Group controlId="statusSelect">
                    <Form.Label htmlFor={'statusSelect'}>Status</Form.Label>
                    <Form.Select
                      id={'statusSelect'}
                      value={meta['STATUS_CODE']}
                      onChange={onStatusChange}
                      required
                    >
                      {Array.from(statusCodes.keys()).map((it) => (
                        <option key={it} value={it}>
                          {it}: {statusCodes.get(it)}
                        </option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Label htmlFor={'delayInput'}>Delay</Form.Label>
                  <InputGroup>
                    <Form.Control
                      type={'number'}
                      id={'delayInput'}
                      min={0}
                      value={`${delay}`}
                      onChange={(e) => setDelay(+e.target.value)}
                      required
                    />
                    <InputGroup.Text>ms</InputGroup.Text>
                  </InputGroup>
                </Col>
                <Col md={2}>
                  <Form.Group controlId="mockTypeSelect">
                    <Form.Label htmlFor={'typeSelect'}>Type</Form.Label>
                    <Form.Select
                      id={'typeSelect'}
                      value={mockType}
                      onChange={onMockTypeChange}
                      required
                    >
                      {
                        Array.from(mockTypes.keys()).map(
                          it => (
                            <option key={it} value={it}>{mockTypes.get(it)}</option>
                          )
                        )
                      }
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>

              {/* Content Section Based on Mock Type */}
              {mockType === 'STATIC' && (
                <StaticMockContent
                  content={content}
                  setContent={setContent}
                  meta={meta}
                  setMeta={setMeta}
                  creation={!isEditMode} />
              )}

              {mockType === 'STATIC_FILE' && <StaticFileMockContent
                content={content}
                setContent={setContent}
                isEditMode={isEditMode}
              />}

              {(mockType === 'JS' || mockType === 'PYTHON') && (
                <GraalVMMockContent
                  mode={mockType === 'JS' ? 'javascript' : 'python'}
                  content={content}
                  setContent={setContent}
                />
              )}

              {/* Submit Button */}
              <Row className="mb-3">
                <Col className="d-flex justify-content-end">
                  <Button
                    variant="primary"
                    type="submit"
                    title={'Save'}
                  >
                    <FloppyDiskIcon />
                  </Button>
                </Col>
              </Row>

              {(mockType === 'JS' || mockType === 'PYTHON') && (
                <CodeDocumentation mode={mockType === 'JS' ? 'javascript' : 'python'} />
              )}
            </Form>
          </Col>
        </Row>
      }
    </Container>
  );
};

export default MockForm;
