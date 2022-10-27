import { css } from '@emotion/react';

import { DEFAULT_STUDY_CARD_QUERY_PARAM } from '@constants';

import { mqDown } from '@styles/responsive';

import Flex from '@shared/flex/Flex';
import Skeleton from '@shared/skeleton/Skeleton';

const StudyCardListSkeleton = () => {
  return (
    <div
      css={css`
        display: grid;
        grid-template-columns: repeat(4, minmax(auto, 1fr));
        grid-template-rows: 1fr;
        gap: 32px;

        ${mqDown('lg')} {
          grid-template-columns: repeat(3, 1fr);
        }
        ${mqDown('md')} {
          grid-template-columns: repeat(2, 1fr);
        }
        ${mqDown('sm')} {
          grid-template-columns: repeat(1, 256px);
          place-content: center;
        }
      `}
    >
      {Array.from({ length: DEFAULT_STUDY_CARD_QUERY_PARAM.SIZE }).map((_, index) => (
        <StudyCardSkeleton key={index} />
      ))}
    </div>
  );
};

export default StudyCardListSkeleton;

export const StudyCardSkeleton = () => {
  return (
    <Flex flexDirection="column" rowGap="8px" custom={{ height: '280px' }}>
      <Skeleton.Rectangular width="100%" height="100%" />
      <Skeleton.Text fontSize="lg" />
      <Skeleton.Text fontSize="sm" custom={{ width: '60%' }} />
      <Flex flexDirection="column" alignItems="flex-end" rowGap="8px">
        <Skeleton.Text fontSize="sm" custom={{ width: '60%' }} />
      </Flex>
    </Flex>
  );
};
