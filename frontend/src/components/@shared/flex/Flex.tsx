import type { Properties } from 'csstype';

import { css, useTheme } from '@emotion/react';

import { type CustomCSS, getResponsiveStyle, resolveCustomCSS } from '@styles/custom-css';
import { type BreakpointsFor } from '@styles/responsive';

export type FlexBoxStyleProperty = {
  alignItems?: Properties['alignItems'];
  justifyContent?: Properties['justifyContent'];
  flexDirection?: Properties['flexDirection'];
  flexWrap?: Properties['flexWrap'];
  rowGap?: Properties['gap'];
  columnGap?: Properties['gap'];
};

export type FlexItemStyleProperty = {
  grow: Properties['flexGrow'];
  custom: CustomCSS<'width' | 'height' | 'maxWidth' | 'maxHeight' | 'minWidth' | 'minHeight'>;
};

export type FlexBoxProps = Partial<
  {
    children: React.ReactNode;
    fluid: boolean;
    custom?: CustomCSS<'width' | 'height' | 'maxWidth' | 'maxHeight' | 'minWidth' | 'minHeight' | 'marginBottom'>;
  } & FlexBoxStyleProperty &
    BreakpointsFor<FlexBoxStyleProperty>
>;

const Flex: React.FC<FlexBoxProps> = ({
  children,
  fluid = false,
  alignItems = 'stretch',
  justifyContent = 'flex-start',
  flexDirection = 'row',
  flexWrap = 'nowrap',
  rowGap = '0',
  columnGap = '0',
  custom,
  ...responsive
}) => {
  const theme = useTheme();
  const { xs, sm, md, lg, xl, xxl, xxxl } = responsive;

  const style = css`
    display: flex;
    width: ${fluid ? '100%' : 'auto'};
    ${resolveCustomCSS(custom, theme)};
    ${resolveCustomCSS(
      {
        alignItems,
        justifyContent,
        flexDirection,
        flexWrap,
        rowGap,
        columnGap,
      },
      theme,
    )};
    ${xxxl && getResponsiveStyle('xxxl', xxxl)};
    ${xxl && getResponsiveStyle('xxl', xxl)};
    ${xl && getResponsiveStyle('xl', xl)};
    ${lg && getResponsiveStyle('lg', lg)};
    ${md && getResponsiveStyle('md', md)};
    ${sm && getResponsiveStyle('sm', sm)};
    ${xs && getResponsiveStyle('xs', xs)};
  `;

  return <div css={style}>{children}</div>;
};

export type FlexItemStyle = {
  flexGrow?: Properties['flexGrow'];
};

export type FlexItemProps = Partial<{ children: React.ReactNode } & FlexItemStyle>;

const FlexItem: React.FC<FlexItemProps> = ({ children, flexGrow = 1 }) => {
  const theme = useTheme();
  return <div css={resolveCustomCSS({ flexGrow }, theme)}>{children}</div>;
};

export default Object.assign(Flex, {
  Item: FlexItem,
});
