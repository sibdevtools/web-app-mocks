import React, { useState } from 'react';
import { TextWrapIcon } from 'hugeicons-react';
import AceEditor from 'react-ace';

import '../const/ace.imports';
import { loadSettings } from '../settings/utils';
import { Button, ButtonGroup, Form } from 'react-bootstrap';

export interface GraalVMMockContentProps {
  mode: string;
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
    <Form.Group className="mb-3">
      <Form.Label htmlFor="contentTextArea">Content</Form.Label>

      <div className="position-relative">
        {/* Word Wrap Button */}
        <ButtonGroup className="position-absolute" style={{ top: '-20px', right: '-8px', zIndex: 3 }}>
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
      </div>
    </Form.Group>
  );
};

const textDecoder = new TextDecoder();

export default GraalVMMockContent;
