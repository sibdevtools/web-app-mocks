import React, { useEffect, useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import {
  ContentType,
  mimeToAceModeMap
} from '../const/common.const';
import AceEditor from 'react-ace';

import '../const/ace.imports'
import { loadSettings } from '../settings/utils';

export interface StaticMockContentProps {
  content: ArrayBuffer,
  setContent: (content: ArrayBuffer) => void,
  meta: { [key: string]: string },
  setMeta: (meta: { [key: string]: string }) => void,
  creation: boolean,
}

const textEncoder = new TextEncoder();
const textDecoder = new TextDecoder();

const StaticMockContent: React.FC<StaticMockContentProps> = ({
                                                               content,
                                                               setContent,
                                                               meta
                                                             }) => {
  const [aceType, setAceType] = useState('text');

  const settings = loadSettings();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);

  useEffect(() => {
    const httpHeadersJson = meta['HTTP_HEADERS'];
    const httpHeaders = httpHeadersJson ? JSON.parse(httpHeadersJson) : null;
    const contentType = (httpHeaders ? httpHeaders['Content-Type'] : null) as ContentType;
    setAceType(mimeToAceModeMap.get(contentType) || 'text');
  }, [meta]);

  return (
    <>
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
              theme={settings['aceTheme'].value}
              name={`contentAceEditor`}
              onChange={it => setContent(textEncoder.encode(it))}
              value={textDecoder.decode(content)}
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
