import React, { useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import AceEditor from 'react-ace';

import '../const/ace.imports';
import { loadSettings } from '../settings/utils';
import { Button, ButtonGroup, Form, Table } from 'react-bootstrap';
import CodeDocumentation from '../pages/mock/CodeDocumentation';

export interface GraalVMMockContentProps {
  mode: 'javascript' | 'python';
  content: ArrayBuffer;
  setContent: (content: ArrayBuffer) => void;
}

const textEncoder = new TextEncoder();
const GraalVMMockContent: React.FC<GraalVMMockContentProps> = ({
                                                                 mode,
                                                                 content,
                                                                 setContent,
                                                               }) => {
  const settings = loadSettings();
  const [isWordWrapEnabled, setIsWordWrapEnabled] = useState(true);

  return (
    <>
      <Form.Group className="mb-3">
        <Form.Label htmlFor="contentTextArea">Content</Form.Label>

        {/* Word Wrap Button */}
        <ButtonGroup className="float-end">
          <Button
            variant="primary"
            active={isWordWrapEnabled}
            title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
            onClick={() => setIsWordWrapEnabled((prev) => !prev)}
          >
            <TextWrapIcon />
          </Button>
        </ButtonGroup>

        {/* Ace Editor */}
        <AceEditor
          mode={mode}
          theme={settings['aceTheme'].value}
          name="contentAceEditor"
          onChange={(it) => setContent(textEncoder.encode(it))}
          value={textDecoder.decode(content)}
          className={'rounded'}
          style={{
            resize: 'vertical',
            overflow: 'auto',
            height: '480px',
            minHeight: '200px',
          }}
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
            useWorker: false,
          }}
          editorProps={{ $blockScrolling: true }}
        />
      </Form.Group>
      <CodeDocumentation mode={mode} />
    </>
  );
};

const textDecoder = new TextDecoder();

export default GraalVMMockContent;
