import React, { useEffect, useState } from 'react';
import { MinusSignIcon, PlusSignIcon } from 'hugeicons-react';
import { mimeToAceModeMap } from '../const/common.const';

interface Header {
  key: string;
  value: string;
}

interface HttpHeadersFormProps {
  meta: { [key: string]: string };
  setMeta: (newMeta: { [key: string]: string }) => void;
}

const commonHeaders = [
  'Content-Type',
  'Authorization',
  'Accept',
  'Cache-Control',
  'User-Agent',
  'Accept-Encoding',
  'Host',
  'Referer',
];

const headerValueSuggestions: { [key: string]: string[] } = {
  'Content-Type': [...mimeToAceModeMap.keys()],
  'Authorization': [
    'Bearer <token>',
    'Basic <credentials>',
  ],
  'Accept': [
    'application/json',
    'text/html',
    'application/xml',
  ],
  'Cache-Control': [
    'no-cache',
    'no-store',
    'must-revalidate',
    'public',
    'private',
  ],
};

const HttpHeadersForm: React.FC<HttpHeadersFormProps> = ({
                                                           meta,
                                                           setMeta
                                                         }) => {
  const initialHeaders = meta['HTTP_HEADERS']
    ? JSON.parse(meta['HTTP_HEADERS'] as string)
    : {};

  const [headers, setHeaders] = useState<Header[]>(
    Object.entries(initialHeaders).length > 0
      ? Object.entries(initialHeaders).map(([key, value]) => ({ key, value: `${value}` }))
      : [{ key: '', value: '' }]
  );

  useEffect(() => {
    const initialHeaders = meta.HTTP_HEADERS
      ? JSON.parse(meta.HTTP_HEADERS)
      : {};

    const updatedHeaders = Object.entries(initialHeaders).length > 0
      ? Object.entries(initialHeaders).map(([key, value]) => ({ key, value: `${value}` }))
      : [{ key: '', value: '' }];

    setHeaders(updatedHeaders);
  }, [meta]);

  const updateMetaHeaders = (updatedHeaders: Header[]) => {
    const headersObject: { [key: string]: string } = {};
    updatedHeaders.forEach(({ key, value }) => {
      headersObject[key] = value;
    });

    const newMeta = { ...meta, HTTP_HEADERS: JSON.stringify(headersObject) };
    setMeta(newMeta);
  };

  const handleHeaderChange = (
    index: number,
    field: 'key' | 'value',
    value: string
  ) => {
    const updatedHeaders = [...headers];
    updatedHeaders[index][field] = value;
    setHeaders(updatedHeaders);
    updateMetaHeaders(updatedHeaders);
  };

  const addHeader = () => {
    const updatedHeaders = [...headers, { key: '', value: '' }];
    setHeaders(updatedHeaders);
    updateMetaHeaders(updatedHeaders);
  };

  const removeHeader = (index: number) => {
    const updatedHeaders = headers.filter((_, i) => i !== index);
    setHeaders(updatedHeaders);
    updateMetaHeaders(updatedHeaders);
  };

  return (
    <>
      {headers.map((header, index) => (
        <div key={index} className="row mb-3">
          <div className="col-md-5">
            <input
              type="text"
              className="form-control"
              list="header-suggestions"
              placeholder="Header Key"
              value={header.key}
              onChange={(e) =>
                handleHeaderChange(index, 'key', e.target.value)
              }
            />
            <datalist id="header-suggestions">
              {commonHeaders.map((headerName, i) => (
                <option key={i} value={headerName} />
              ))}
            </datalist>
          </div>
          <div className="col-md-5">
            <input
              type="text"
              className="form-control"
              list={`value-suggestions-${index}`}
              placeholder="Header Value"
              value={header.value}
              onChange={(e) =>
                handleHeaderChange(index, 'value', e.target.value)
              }
            />
            <datalist id={`value-suggestions-${index}`}>
              {(headerValueSuggestions[header.key] || []).map(
                (valueSuggestion, i) => (
                  <option key={i} value={valueSuggestion} />
                )
              )}
            </datalist>
          </div>
          <div className="col-md-2">
            <div className="btn-group"
                 role="group">
              <button
                className="btn btn-danger"
                onClick={() => removeHeader(index)}
                disabled={headers.length === 1}
              >
                <MinusSignIcon />
              </button>
              {
                (index === headers.length - 1) && (
                  <button
                    className="btn btn-primary"
                    onClick={addHeader}
                  >
                    <PlusSignIcon />
                  </button>
                )
              }
            </div>
          </div>
        </div>
      ))}
    </>
  );
};

export default HttpHeadersForm;
