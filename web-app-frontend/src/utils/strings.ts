/**
 * Case-insensitive string comparison
 * @param a left argument
 * @param b right argument
 */
export const caseInsensitiveEquals = (a: string, b: string): boolean => {
  return a.localeCompare(b, undefined, { sensitivity: 'accent' }) === 0;
};
