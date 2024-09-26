import React, { useEffect, useState } from 'react';
import { getMock, updateMock } from '../../api/service';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft01Icon, FloppyDiskIcon } from 'hugeicons-react';
import {
  contextPath,
  Method,
  methods, MockType, mockTypes,
  StatusCode,
  statusCodes
} from '../../const/common.const';
import { encodeTextToBase64 } from '../../utils/base.64converters';
import StaticMockContent from '../../componenets/StaticMockContent';
import JavaScriptMockContent from '../../componenets/JavaScriptMockContent';
import PythonMockContent from '../../componenets/PythonMockContent';
import HttpHeadersForm from '../../componenets/HttpHeadersForm';
import StaticFileMockContent from '../../componenets/StaticFileMockContent';

const textEncoder = new TextEncoder()

const EditMockPage: React.FC = () => {
  const navigate = useNavigate();
  const { serviceId, mockId } = useParams();
  const [mockName, setMockName] = useState('');
  const [method, setMethod] = useState<Method>(methods[0]);
  const [path, setPath] = useState('');
  const [mockType, setMockType] = useState<MockType>('STATIC');
  const [meta, setMeta] = useState<{ [key: string]: string }>({
    STATUS_CODE: '200'
  });
  const [content, setContent] = useState(new ArrayBuffer(0));

  useEffect(() => {
    if (!serviceId || !mockId) {
      return
    }
    fetchMock();
  }, []);

  if (!serviceId || !mockId) {
    navigate(contextPath);
    return;
  }

  const fetchMock = async () => {
    try {
      const response = await getMock(+serviceId, +mockId);
      if (response.data.success) {
        const body = response.data.body

        setMockName(body.name);
        setMethod(body.method);
        setPath(body.path);
        setMockType(body.type);
        setContent(textEncoder.encode(body.content));
        setMeta(body.meta)
      }
    } catch (error) {
      console.error('Failed to fetch services:', error);
    }
  };

  const toServicePage = () => {
    navigate(`${contextPath}service/${serviceId}/mocks`);
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await updateMock(+serviceId, +mockId, {
        name: mockName,
        method: method,
        path: path,
        type: mockType,
        meta: meta,
        content: encodeTextToBase64(content)
      });
      toServicePage();
    } catch (error) {
      console.error('Failed to create service:', error);
    }
  };

  const onStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newValue = +e.target.value;
    const newStatusCode = newValue as StatusCode;
    if (newStatusCode) {
      setMeta({ ...meta, STATUS_CODE: `${newStatusCode}` })
    }
  }

  const onMockTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newValue = e.target.value;
    const newMockType = newValue as MockType;
    if (newMockType) {
      setMockType(newMockType)
    }
  }

  return (
    <div className="container mt-4 mb-4">
      <div className={'row'}>
        <div className={'col-md-1 offset-md-2 mb-2'}>
          <button type="button" className="btn btn-outline-primary" onClick={() => toServicePage()}>
            <ArrowLeft01Icon />
          </button>
        </div>
        <div className={'col-md-9'}>
          <h2>Edit Mock</h2>
        </div>
      </div>
      <div className={'row'}>
        <div className="col-md-10 offset-md-1">
          <form className={'mt-4'} onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="mockNameInput" className="form-label">Name</label>
              <input
                type="text"
                className="form-control"
                id="mockNameInput"
                value={mockName}
                onChange={(e) => setMockName(e.target.value)}
                required={true}
              />
            </div>
            <div className={'row mb-3'}>
              <div className="col-md-2">
                <label htmlFor="httpMethodSelect" className="form-label">HTTP Method</label>
                <select
                  id={'httpMethodSelect'}
                  className={'form-select'}
                  onChange={e => setMethod(methods[e.target.selectedIndex])}
                  value={method}
                  required={true}
                >
                  {
                    methods.map((it) => (
                        <option key={it} value={it}>{it}</option>
                      )
                    )
                  }
                </select>
              </div>
              <div className="col-md-10">
                <label htmlFor="pathInput" className="form-label">Path</label>
                <input
                  type="text"
                  className="form-control"
                  id="pathInput"
                  placeholder={'Ant pattern or path, started with /'}
                  value={path}
                  onChange={(e) => setPath(e.target.value)}
                  required={true}
                />
              </div>
            </div>
            <div className={'row mb-3'}>
              <div className="col-md-12">
                <label htmlFor="httpHeaders" className="form-label">Http Headers</label>
                <HttpHeadersForm
                  meta={meta}
                  setMeta={setMeta}
                />
              </div>
            </div>
            <div className={'row mb-3'}>
              <div className="col-md-8">
                <label htmlFor="statusSelect" className="form-label">Status</label>
                <select
                  id={'statusSelect'}
                  className={'form-select'}
                  value={meta['STATUS_CODE']}
                  onChange={(e) => onStatusChange(e)}
                  required={true}
                >
                  {
                    [...statusCodes.keys()].map(it => (
                        <option key={it} value={it}>{it}: {statusCodes.get(it)}</option>
                      )
                    )
                  }
                </select>
              </div>
              <div className={'col-md-2'}>
                <label htmlFor="mockTypeSelect" className="form-label">Type</label>
                <select
                  id={'mockTypeSelect'}
                  className={'form-select'}
                  value={mockType}
                  onChange={(e) => onMockTypeChange(e)}
                  required={true}
                >
                  {
                    mockTypes.map(it => (
                        <option key={it} value={it}>{it}</option>
                      )
                    )
                  }
                </select>
              </div>
            </div>
            {
              (mockType === 'STATIC') ? (
                <StaticMockContent
                  content={content}
                  setContent={setContent}
                  meta={meta}
                  setMeta={setMeta}
                  creation={false}
                />
              ) : (mockType === 'STATIC_FILE') ? (
                <StaticFileMockContent
                  setContent={setContent}
                />
              ) : (mockType === 'JS') ? (
                <JavaScriptMockContent
                  content={content}
                  setContent={setContent}
                />
              ) : (
                <PythonMockContent
                  content={content}
                  setContent={setContent} />
              )
            }
            <div className={'col-md-1 offset-md-11'}>
              <button type="submit" className="btn btn-primary">
                <FloppyDiskIcon />
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditMockPage;
