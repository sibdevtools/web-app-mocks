export const encodeTextToBase64 = (text: string, encoding: string): string => {
  const encoder = new TextEncoder();
  const encoded = encoder.encode(text);
  return btoa(String.fromCharCode(...encoded));
};

export const decodeBase64ToText = (base64: string, encoding: string): string => {
  const decoded = atob(base64);
  const decoder = new TextDecoder();
  return decoder.decode(Uint8Array.from(decoded.split(''), c => c.charCodeAt(0)));
};

