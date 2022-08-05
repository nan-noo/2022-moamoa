import DOMPurify from 'dompurify';
import { marked } from 'marked';
import { useEffect, useRef } from 'react';

import { css } from '@emotion/react';

import markdown from '@styles/markdown';

type MarkdownRenderProps = {
  markdownContent: string;
};

const MarkdownRender = ({ markdownContent }: MarkdownRenderProps) => {
  const contentRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (!contentRef.current) return;
    const cleanHtml = DOMPurify.sanitize(marked.parse(markdownContent));
    contentRef.current.innerHTML = cleanHtml;
  }, [contentRef, markdownContent]);

  return (
    <div
      css={css`
        overflow-y: scroll;
        height: 100%;
      `}
    >
      <div
        css={css`
          ${markdown}
        `}
        ref={contentRef}
      ></div>
    </div>
  );
};

export default MarkdownRender;
