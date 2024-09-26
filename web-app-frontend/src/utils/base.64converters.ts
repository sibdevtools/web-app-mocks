/**
 * Encode array buffer to base64
 * @param arrayBuffer array buffer
 */
export const encodeTextToBase64 = (arrayBuffer: ArrayBuffer): string => {
  return btoa(new Uint8Array(arrayBuffer).reduce(function (data, byte) {
    return data + String.fromCharCode(byte);
  }, ''));
};

/**
 * Encode base64 to array buffer
 * @param base64 base64 text
 */
export const decodeBase64ToText = (base64: string): ArrayBuffer => {
  const binaryString = atob(base64);
  const len = binaryString.length;
  const bytes = new Uint8Array(len);

  for (let i = 0; i < len; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }

  return bytes.buffer;
};
