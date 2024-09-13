export interface ValidationStatus {
  status: boolean,
  message: string | any
}

export const validateJson = (inputJson: string): ValidationStatus => {
  if (!inputJson) {
    return {
      status: false,
      message: 'Is empty'
    };
  }
  try {
    const parsed = JSON.parse(inputJson);
    return {
      status: true,
      message: parsed
    };
  } catch (error) {
    console.error(`Validation error`, error);
    return {
      status: false,
      message: `Validation error: ${error}`
    };
  }
};

export interface PrettifyStatus {
  status: boolean,
  message: string | any
}

export const prettifyJson = (inputJson: string): PrettifyStatus => {
  const validationStatus = validateJson(inputJson);
  if (!validationStatus.status) {
    return validationStatus;
  }
  const validated = validationStatus.message
  try {
    const prettified = JSON.stringify(validated, null, 2);
    return {
      status: true,
      message: prettified
    };
  } catch (error) {
    console.error(`Prettify error`, error);
    return {
      status: false,
      message: `Prettify error: ${error}`
    };
  }
};
