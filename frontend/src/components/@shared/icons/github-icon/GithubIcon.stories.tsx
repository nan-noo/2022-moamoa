import { type Story } from '@storybook/react';

import { GithubIcon } from '@shared/icons';

export default {
  title: 'Materials/Icons/GithubIcon',
  component: GithubIcon,
};

const Template: Story = () => <GithubIcon />;

export const Default = Template.bind({});
Default.args = {};
