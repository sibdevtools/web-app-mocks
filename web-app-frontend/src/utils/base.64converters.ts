/**
 * Encode array buffer to base64
 * @param arrayBuffer array buffer
 */
export const encodeTextToBase64 = (arrayBuffer: ArrayBuffer): string => {
  return btoa(new Uint8Array(arrayBuffer).reduce(function (data, byte) {
    return data + String.fromCharCode(byte);
  }, ''));
};


