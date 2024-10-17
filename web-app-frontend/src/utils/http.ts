import { ContentType } from '../const/common.const';
import { caseInsensitiveEquals } from './strings';

/**
 * Get content type from JSON headers line
 * @param httpHeadersJson JSON headers
 * @returns content type or null
 */
export const getContentType = (httpHeadersJson: string | null): ContentType | null => {
  if (!httpHeadersJson) {
    return null;
  }
  const httpHeaders = httpHeadersJson ? JSON.parse(httpHeadersJson) : null;
  const contentTypeHeader = Object.entries(httpHeaders || {})
    .find(it => caseInsensitiveEquals(it[0], 'content-type'));

  const contentTypeValues = contentTypeHeader?.at(1);
  let contentTypeValue: string | null = null;
  if (typeof contentTypeValues === 'string') {
    contentTypeValue = contentTypeValues;
  } else if (Array.isArray(contentTypeValues)) {
    contentTypeValue = contentTypeValues.at(0);
  } else {
    console.error(`Invalid content-type header type: ${typeof contentTypeValues}`);
  }

  if (!contentTypeValue) {
    return null;
  }

  const contentTypeParts = contentTypeValue.split(';');
  return contentTypeParts?.at(0)
    ?.trim()
    ?.toLowerCase() as ContentType;
};
