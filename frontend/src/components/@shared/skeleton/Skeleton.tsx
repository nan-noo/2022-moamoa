import type { Properties } from 'csstype';

import { css, useTheme } from '@emotion/react';

import { type ThemeFontSize } from '@styles/theme';

import { AVATAR_SIZE } from '@shared/avatar/Avatar';

const applySkeletonAnimation = () => css`
  animation: pulsate 1.5s ease-in-out 0.5s infinite;

  @keyframes pulsate {
    0% {
      opacity: 1;
    }
    50% {
      opacity: 0.4;
    }
    100% {
      opacity: 1;
    }
  }
`;

export type RectangularSkeletonProps = {
  width: Properties['width'];
  height: Properties['height'];
};
const RectangularSkeleton: React.FC<RectangularSkeletonProps> = ({ width, height }) => {
  const theme = useTheme();

  return (
    <div
      css={css`
        width: ${width};
        height: ${height};

        border-radius: ${theme.radius.sm};
        background-color: ${theme.colors.secondary.base};

        ${applySkeletonAnimation()}
      `}
    />
  );
};

export type CircularSkeletonProps = {
  size: keyof typeof AVATAR_SIZE;
};
const CircularSkeleton: React.FC<CircularSkeletonProps> = ({ size }) => {
  const theme = useTheme();

  return (
    <div
      css={css`
        width: ${AVATAR_SIZE[size]};
        height: ${AVATAR_SIZE[size]};

        border-radius: ${theme.radius.circle};
        background-color: ${theme.colors.secondary.base};

        ${applySkeletonAnimation()}
      `}
    />
  );
};

export type TextSkeletonProps = {
  fontSize: ThemeFontSize;
};
const TextSkeleton: React.FC<TextSkeletonProps> = ({ fontSize }) => {
  const theme = useTheme();

  return (
    <div
      css={css`
        height: ${theme.fontSize[fontSize]};

        line-height: ${theme.fontSize[fontSize]};
        border-radius: ${theme.radius.xs};
        background-color: ${theme.colors.secondary.base};

        ${applySkeletonAnimation()}
      `}
    />
  );
};

const Skeleton = {
  Rectangular: RectangularSkeleton,
  Circular: CircularSkeleton,
  Text: TextSkeleton,
};

export default Skeleton;
