import React, { useState } from 'react';
import { Exchange01Icon, TextWrapIcon } from 'hugeicons-react';
import { InputJsonHandle } from './InputJson';
import AceEditor from 'react-ace';
import { useTheme } from '../theme/ThemeContext';

export interface OutputJsonProps {
  setErrorMessage: (inputText: string) => void;
  inputTextRef: React.MutableRefObject<InputJsonHandle | null>;
  inputSpecificationRef: React.MutableRefObject<InputJsonHandle | null>;
}

export const OutputJson = ({
                             setErrorMessage,
                             inputTextRef,
                             inputSpecificationRef
                           }: OutputJsonProps) => {
  const { theme } = useTheme();
  const [outputText, setOutputText] = useState('');
  const [isWordWrapEnabled, setWordWrapEnabled] = useState(true);

  const handleTransform = async () => {
    let input = inputTextRef?.current?.getValidated();
    if (!input) {
      return;
    }

    let specification = inputSpecificationRef?.current?.getValidated();
    if (!specification) {
      return;
    }

    try {
      const response = await fetch('/web/app/jolt/v1/transform', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          input: input,
          specification: specification
        }),
      });

      if (!response.ok) {
        console.error(`Failed to process: ${response.status}`);
        setErrorMessage(`Failed to process: ${response.status}`);
        return;
      }

      const result = await response.json();
      setOutputText(JSON.stringify(result, null, 4));
      setErrorMessage('');
    } catch (error) {
      console.error(error);
      setErrorMessage(`Failed to transform: ${error}`);
    }
  };

  const toggleWordWrap = () => {
    setWordWrapEnabled((prev) => !prev);
  };

  return (
    <>
      <label htmlFor="outputArea" className="form-label">Output</label>
      <div className="btn-group position-absolute" role="group" style={{ top: 0, right: 0, zIndex: 3 }}>
        <button className="btn btn-success" onClick={handleTransform}>
          <Exchange01Icon />
        </button>
        <button
          className={`btn btn-primary ${(isWordWrapEnabled ? 'active' : '')}`}
          title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
          onClick={toggleWordWrap}
        >
          <TextWrapIcon />
        </button>
      </div>
      <div style={{ position: 'relative' }}>
        <AceEditor
          mode="json"
          theme={theme}
          name="outputAceEditor"
          value={outputText}
          fontSize={14}
          width="100%"
          height="480px"
          readOnly
          wrapEnabled={isWordWrapEnabled}
          setOptions={{
            wrap: isWordWrapEnabled,
            useWorker: false
          }}
          editorProps={{ $blockScrolling: true }}
        />
      </div>
    </>
  );
};
