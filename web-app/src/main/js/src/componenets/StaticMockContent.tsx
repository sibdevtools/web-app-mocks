import React, { useEffect, useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import {
  ContentType,
  contentTypes
} from '../const/common.const';
import AceEditor from 'react-ace';
import { useTheme } from '../theme/ThemeContext';

import '../const/ace.imports'

export interface StaticMeta {
  contentType: ContentType
}

export interface StaticMockContentProps {
  content: string,
  setContent: (content: string) => void,
  meta: { [key: string]: string },
  setMeta: (meta: { [key: string]: string }) => void,
}

const StaticMockContent: React.FC<StaticMockContentProps> = ({
                                                               content,
                                                               setContent,
                                                               meta,
                                                               setMeta
                                                             }) => {
  const [localMeta, setLocalMeta] = useState<StaticMeta>({
    contentType: 'application/json'
  });
  const [aceType, setAceType] = useState(contentTypes.get('application/json')?.aceType);

  console.log("update");
  useEffect(() => {
    const basicHttpHeaders = meta['HTTP_HEADERS'];
    const basicHttpHeaderParsed = basicHttpHeaders ? JSON.parse(basicHttpHeaders) : null;
    const contentType = ((basicHttpHeaderParsed ? basicHttpHeaderParsed['Content-Type'] : null) || 'application/json') as ContentType;
    setLocalMeta({ ...localMeta, contentType: contentType })
    setAceType(contentTypes.get(contentType)?.aceType);
  }, [meta]);

  const { theme } = useTheme();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);

  const onContentTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newContentType = e.target.value;
    if (newContentType) {
      setLocalMeta({ ...localMeta, contentType: newContentType })
      setMeta({
        ...meta,
        HTTP_HEADERS: JSON.stringify({
          'Content-Type': newContentType
        })
      })
    }
  }

  return (
    <>
      <div className={'row mb-3'}>
        <div className={'col-md-3'}>
          <label htmlFor="contentTypeSelect" className="form-label">Content Type</label>
          <select
            id={'contentTypeSelect'}
            className={'form-select'}
            value={localMeta.contentType}
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
              onClick={_ => setIsWordWrapEnabled((prev) => !prev)}
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
              mode={aceType}
              theme={theme}
              name={`contentAceEditor`}
              onChange={setContent}
              value={content}
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
    </>
  );
};

export default StaticMockContent;
