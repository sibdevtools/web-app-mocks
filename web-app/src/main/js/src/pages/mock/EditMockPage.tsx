import React, { useEffect, useState } from 'react';
import { updateMock, getMock } from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft01Icon, FloppyDiskIcon, TextWrapIcon } from 'hugeicons-react';
import {
  ContentType,
  contentTypes,
  contextPath,
  Method,
  methods,
  StatusCode,
  statusCodes
} from '../../const/common.const';
import AceEditor from 'react-ace';
import { useTheme } from '../../theme/ThemeContext';
import { decodeBase64ToText, encodeTextToBase64 } from '../../utils/base.64converters';


export interface StaticMeta {
  statusCode: StatusCode
  contentType: ContentType
}

const EditMockPage: React.FC = () => {
  const navigate = useNavigate();
  const { serviceId, mockId } = useParams();

  if (!serviceId || !mockId) {
    navigate(contextPath);
    return;
  }

  useEffect(() => {
    fetchMock();
  }, []);

  const [mockName, setMockName] = useState('');
  const [method, setMethod] = useState<Method>(methods[0]);
  const [antPattern, setAntPattern] = useState('');
  const [meta, setMeta] = useState<StaticMeta>({
    statusCode: 200,
    contentType: 'application/json'
  });
  const { theme } = useTheme();
  const [inputText, setInputText] = useState('');

  const fetchMock = async () => {
    try {
      const response = await getMock(+serviceId, +mockId);
      if (response.data.success) {
        const body = response.data.body
        const httpHeaders = JSON.parse(body.meta['HTTP_HEADERS']);
        console.log('httpHeaders', httpHeaders)
        setMockName(body.name);
        setMethod(body.method);
        setAntPattern(body.antPattern);

        const newMeta = meta
        newMeta.contentType = httpHeaders['Content-Type'] as ContentType

        setInputText(decodeBase64ToText(body.content, 'UTF-8'));

        const newStatusCode = +body.meta['STATUS_CODE'] as StatusCode;
        if (newStatusCode) {
          newMeta.statusCode = newStatusCode
        }
        setMeta(newMeta)
      }
    } catch (error) {
      console.error('Failed to fetch services:', error);
    }
  };

  const toServicePage = () => {
    navigate(`${contextPath}service/${serviceId}/mocks`);
  }

  const [isWordWrapEnabled, setWordWrapEnabled] = useState(true);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await updateMock(+serviceId, +mockId, {
        name: mockName,
        method: method,
        antPattern: antPattern,
        type: 'STATIC',
        meta: {
          STATUS_CODE: `${meta.statusCode}`,
          HTTP_HEADERS: JSON.stringify({
            'Content-Type': meta.contentType
          })
        },
        content: encodeTextToBase64(inputText, 'UTF-8')
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
      setMeta({ ...meta, statusCode: newStatusCode })
    }
  }

  const onContentTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newValue = e.target.value;
    const newContentType = newValue as ContentType;
    if (newContentType) {
      setMeta({ ...meta, contentType: newContentType })
    }
  }

  return (
    <div className="container mt-4">
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
              <div className="col">
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
                        <option value={it}>{it}</option>
                      )
                    )
                  }
                </select>
              </div>
              <div className="col">
                <label htmlFor="statusSelect" className="form-label">Status</label>
                <select
                  id={'statusSelect'}
                  className={'form-select'}
                  value={meta.statusCode}
                  onChange={(e) => onStatusChange(e)}
                  required={true}
                >
                  {
                    [...statusCodes.keys()].map(it => (
                        <option value={it}>{it}: {statusCodes.get(it)}</option>
                      )
                    )
                  }
                </select>
              </div>
            </div>
            <div className={'row mb-3'}>
              <div className="col-md-9">
                <label htmlFor="antPatternInput" className="form-label">Ant Pattern</label>
                <input
                  type="text"
                  className="form-control"
                  id="antPatternInput"
                  value={antPattern}
                  onChange={(e) => setAntPattern(e.target.value)}
                  required={true}
                />
              </div>
              <div className={'col-md-3'}>
                <label htmlFor="contentTypeSelect" className="form-label">Content Type</label>
                <select
                  id={'contentTypeSelect'}
                  className={'form-select'}
                  value={meta.contentType}
                  onChange={(e) => onContentTypeChange(e)}
                  required={true}
                >
                  {
                    [...contentTypes.keys()].map(it => (
                        <option value={it}>{contentTypes.get(it)?.caption}</option>
                      )
                    )
                  }
                </select>
              </div>
            </div>
            <div className={'mb-3'}>
              <label htmlFor={`contentTextArea`} className="form-label">Content</label>
              <div className={' position-relative'}>
                <div className="btn-group position-absolute" role="group"
                     style={{ top: '-20px', right: '-8px', zIndex: 3 }}>
                  <button
                    className={`btn btn-primary ${(isWordWrapEnabled ? 'active' : '')}`}
                    title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
                    type={'button'}
                    onClick={_ => setWordWrapEnabled((prev) => !prev)}
                  >
                    <TextWrapIcon />
                  </button>
                </div>
                <div id={`contentTextArea`}
                     style={{ position: 'relative' }}
                     aria-describedby={`contentTextAreaFeedback`}
                     className={`form-control`}
                >
                  <AceEditor
                    mode={contentTypes.get(meta.contentType)?.aceType}
                    theme={theme}
                    name={`contentAceEditor`}
                    onChange={setInputText}
                    value={inputText}
                    fontSize={14}
                    width="100%"
                    height="480px"
                    showPrintMargin={true}
                    showGutter={true}
                    highlightActiveLine={true}
                    wrapEnabled={isWordWrapEnabled}
                    setOptions={{
                      enableBasicAutocompletion: true,
                      enableLiveAutocompletion: true,
                      showLineNumbers: true,
                      enableSnippets: true,
                      wrap: isWordWrapEnabled,
                      useWorker: false
                    }}
                    editorProps={{ $blockScrolling: true }}
                  />
                </div>
              </div>
            </div>
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
