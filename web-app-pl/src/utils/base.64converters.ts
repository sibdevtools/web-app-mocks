/**
 * Encode text to base64
 * @param text text
 * @param encoding encoding
 */
export const encodeTextToBase64 = (text: string, encoding: string): string => {
  const encoder = new TextEncoder();
  const encoded = encoder.encode(text);
  return btoa(String.fromCharCode(...encoded));
};

/**
 * Decode base64 to source text
 * @param base64 encoded
 * @param encoding encoding
 */
export const decodeBase64ToText = (base64: string, encoding: string): string => {
  const decoded = atob(base64);
  const decoder = new TextDecoder();
  return decoder.decode(Uint8Array.from(decoded.split(''), c => c.charCodeAt(0)));
};

