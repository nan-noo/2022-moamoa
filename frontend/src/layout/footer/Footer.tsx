import { css, useTheme } from '@emotion/react';

import { type CustomCSS } from '@styles/custom-css';

import Flex from '@shared/flex/Flex';
import { GithubIcon } from '@shared/icons';

export type FooterProps = {
  custom?: CustomCSS<'marginBottom'>;
};

const Footer: React.FC<FooterProps> = ({ custom }) => {
  return (
    <Flex
      justifyContent="center"
      alignItems="center"
      columnGap="32px"
      custom={{
        padding: '24px 0',
        ...custom,
      }}
    >
      <Description />
      <GithubLink />
    </Flex>
  );
};

export default Footer;

const Description: React.FC = () => {
  const theme = useTheme();

  return (
    <div
      css={css`
        color: ${theme.colors.secondary.dark};
      `}
    >
      Copyright © 2022 MOAMOA
    </div>
  );
};

const GithubLink: React.FC = () => {
  const theme = useTheme();

  return (
    <a
      href="https://github.com/woowacourse-teams/2022-moamoa"
      target="_blank"
      rel="noreferrer"
      css={css`
        padding: 4px;

        border-radius: ${theme.radius.circle};

        &:hover {
          & > svg {
            fill: ${theme.colors.black};
          }
        }

        & > svg {
          fill: ${theme.colors.secondary.dark};

          transition: fill 0.2s ease;
        }
      `}
    >
      <GithubIcon />
    </a>
  );
};
