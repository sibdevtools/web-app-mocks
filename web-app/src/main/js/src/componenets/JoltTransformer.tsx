import React, { useRef, useState } from 'react';
import { InputJson, InputJsonHandle } from './InputJson';
import { OutputJson } from './OutputJson';
import { useTheme } from "../theme/ThemeContext";

export const JoltTransformer = () => {
  const inputTextRef = useRef<InputJsonHandle>(null);
  const inputSpecificationRef = useRef<InputJsonHandle>(null);

  const [errorMessage, setErrorMessage] = useState('');
  const { theme, setTheme } = useTheme()

  const handleThemeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setTheme(event.target.value);
  };

  return (
    <div className="container mt-5">
      {errorMessage && (
        <div className="row">
          <div className="row mt-3">
            <div className="col">
              <div className="alert alert-danger" role="alert">
                {errorMessage}
              </div>
            </div>
          </div>
        </div>
      )}
      <div className="row mb-3">
        <div className="col-12">
          <label htmlFor="themeSelect">Select Theme:</label>
          <select
            id="themeSelect"
            className="form-control"
            value={theme}
            onChange={handleThemeChange}
          >
            <option value="monokai">Monokai</option>
            <option value="github">GitHub</option>
            <option value="tomorrow">Tomorrow</option>
            <option value="kuroir">Kuroir</option>
            <option value="twilight">Twilight</option>
            <option value="xcode">Xcode</option>
            <option value="textmate">Textmate</option>
            <option value="solarized_dark">Solarized Dark</option>
            <option value="solarized_light">Solarized Light</option>
            <option value="terminal">Terminal</option>
          </select>
        </div>
      </div>
      <div className="row">
        <div className="col-4 position-relative">
          <InputJson
            ref={inputTextRef}
            id="input"
            name="Input"
          />
        </div>
        <div className="col-4 position-relative">
          <InputJson
            ref={inputSpecificationRef}
            id="inputSpecification"
            name="Specification"
          />
        </div>
        <div className="col-4 position-relative">
          <OutputJson
            inputTextRef={inputTextRef}
            inputSpecificationRef={inputSpecificationRef}
            setErrorMessage={setErrorMessage}
          />
        </div>
      </div>
    </div>
  );
};
