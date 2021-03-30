/*
  <div *xl>HELLO XL</div>
  <div *lg>HELLO LG</div>
  <div *md>HELLO MD</div>
  <div *sm>HELLO SM</div>
  <div *xs>HELLO XS</div>
*/
export class ScreenSizeValues {
    public static readonly XS = 'xs';
    public static readonly SM = 'sm';
    public static readonly MD = 'md';
    public static readonly LG = 'lg';
    public static readonly XL = 'xl';
}

export type ScreenSizeType = 'xs' | 'md' | 'lg' | 'xl';

