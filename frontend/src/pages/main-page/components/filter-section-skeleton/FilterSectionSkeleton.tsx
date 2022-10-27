import Divider from '@shared/divider/Divider';
import Skeleton from '@shared/skeleton/Skeleton';

import Flex from '@components/@shared/flex/Flex';

const FilterSectionSkeleton: React.FC = () => {
  return (
    <>
      <FilterButtonListSkeleton count={2} />
      <Divider orientation="vertical" verticalLength="40px" />
      <FilterButtonListSkeleton count={2} />
      <Divider orientation="vertical" verticalLength="40px" />
      <FilterButtonListSkeleton count={9} />
    </>
  );
};

export default FilterSectionSkeleton;

const FilterButtonListSkeleton: React.FC<{ count: number }> = ({ count }) => {
  return (
    <Flex columnGap="12px">
      {Array.from({ length: count }).map((_, index) => (
        <FilterButtonSkeleton key={index} />
      ))}
    </Flex>
  );
};

const FilterButtonSkeleton: React.FC = () => {
  return (
    <Flex flexDirection="column" alignItems="center" rowGap="4px" custom={{ width: '84px', height: '78px' }}>
      <Skeleton.Circular size="md" />
      <Skeleton.Text fontSize="sm" />
    </Flex>
  );
};
