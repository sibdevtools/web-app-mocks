import React, { forwardRef, useImperativeHandle, useState } from 'react';
import { AiBeautifyIcon, CheckmarkSquare01Icon, TextWrapIcon } from 'hugeicons-react';
import { prettifyJson, validateJson } from '../utils/validators';
import AceEditor from 'react-ace';
import { useTheme } from '../theme/ThemeContext';

export interface InputJsonProps {
  id: string;
  name: string;
}

export interface InputJsonHandle {
  getValidated: () => string;
}

export const InputJson = forwardRef<InputJsonHandle, InputJsonProps>(
  ({ id, name }: InputJsonProps, ref) => {
    const { theme } = useTheme();
    const [inputText, setInputText] = useState('');
    const [isInputTextInvalid, setInputTextInvalid] = useState('');
    const [isInputTextValid, setInputTextValid] = useState(false);
    const [isWordWrapEnabled, setWordWrapEnabled] = useState(true);

    useImperativeHandle(ref, () => ({
      getValidated: () => {
        return validateInputText();
      },
    }));

    const handleInputChange = (newValue: string) => {
      setInputText(newValue);
    };

    const validateInputText = () => {
      const validationStatus = validateJson(inputText);
      if (validationStatus.status) {
        setInputTextInvalid('');
        setInputTextValid(true);
        return validationStatus.message;
      }
      setInputTextInvalid(validationStatus.message);
      setInputTextValid(false);
      return null;
    };

    const prettifyInputText = () => {
      const prettifyStatus = prettifyJson(inputText);
      if (prettifyStatus.status) {
        setInputTextInvalid('');
        setInputTextValid(true);
        setInputText(prettifyStatus.message);
      } else {
        setInputTextInvalid(prettifyStatus.message);
        setInputTextValid(false);
      }
    };

    const toggleWordWrap = () => {
      setWordWrapEnabled((prev) => !prev);
    };

    return (
      <>
        <label htmlFor={`${id}TextArea`} className="form-label">{name}</label>
        <div className="btn-group position-absolute" role="group" style={{ top: 0, right: 0, zIndex: 3 }}>
          <button className="btn btn-primary" title="Beautify" onClick={prettifyInputText}>
            <AiBeautifyIcon />
          </button>
          <button
            className="btn btn-success"
            title="Validate"
            onClick={validateInputText}>
            <CheckmarkSquare01Icon />
          </button>
          <button
            className={`btn btn-primary ${(isWordWrapEnabled ? 'active' : '')}`}
            title={isWordWrapEnabled ? 'Unwrap' : 'Wrap'}
            onClick={toggleWordWrap}
          >
            <TextWrapIcon />
          </button>
        </div>
        <div id={`${id}TextArea`}
             style={{ position: 'relative' }}
             aria-describedby={`${id}TextAreaFeedback`}
             className={`form-control ${(isInputTextInvalid === '' ? '' : 'is-invalid')} ${(isInputTextValid ? 'is-valid' : '')}`}
        >
          <AceEditor
            mode="json"
            theme={theme}
            name={`${id}AceEditor`}
            onChange={handleInputChange}
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
        <div id={`${id}TextAreaFeedback`} className="invalid-feedback">
          {isInputTextInvalid}
        </div>
      </>
    );
  }
);
