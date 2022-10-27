import { type Story } from '@storybook/react';

import Skeleton, {
  type CircularSkeletonProps,
  type RectangularSkeletonProps,
  type TextSkeletonProps,
} from '@components/@shared/skeleton/Skeleton';

export default {
  title: 'Components/Skeleton',
  component: Skeleton,
};

const RectangularTemplate: Story<RectangularSkeletonProps> = props => <Skeleton.Rectangular {...props} />;

export const Rectangular = RectangularTemplate.bind({});
Rectangular.args = {
  width: '100px',
  height: '50px',
};

const CircularTemplate: Story<CircularSkeletonProps> = props => <Skeleton.Circular {...props} />;

export const Circular = CircularTemplate.bind({});
Circular.args = {
  size: 'sm',
};

const TextTemplate: Story<TextSkeletonProps> = props => <Skeleton.Text {...props} />;

export const Text = TextTemplate.bind({});
Text.args = {
  fontSize: 'xs',
  custom: {
    width: '60%',
  },
};
