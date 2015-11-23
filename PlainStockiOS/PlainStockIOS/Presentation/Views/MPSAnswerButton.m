#import "MPSAnswerButton.h"

#define BG_COLOR_NORMAL [UIColor colorWithRed:239.0/255.0 green:239.0/255.0 blue:244.0/255.0 alpha:1.0]
#define BG_COLOR_HIGHLIGHTED [UIColor colorWithRed:0.0 green:128.0/255.0 blue:1.0 alpha:1.0]
#define TITLE_COLOR_NORMAL [UIColor colorWithRed:0.0 green:128.0/255.0 blue:1.0 alpha:1.0]
#define TITLE_COLOR_HIGHLIGHTED [UIColor whiteColor]


@implementation MPSAnswerButton

-(void) setup
{
    //sets color and border
    [self setBackgroundColor: BG_COLOR_NORMAL];
    [self setTitleColor: TITLE_COLOR_NORMAL forState:UIControlStateNormal];
    [self setTitleColor: TITLE_COLOR_HIGHLIGHTED forState:UIControlStateHighlighted];
    [self setTitleColor: TITLE_COLOR_HIGHLIGHTED forState:UIControlStateSelected];
}

-(void) setHighlighted:(BOOL)highlighted
{
    if(highlighted)
    {
        self.backgroundColor = BG_COLOR_HIGHLIGHTED;
    }
    else
    {
        if (!self.selected)
            self.backgroundColor = BG_COLOR_NORMAL;
    }
    [super setHighlighted:highlighted];
}

-(void) setSelected:(BOOL)selected
{
    if(selected)
    {
        self.backgroundColor = BG_COLOR_HIGHLIGHTED;
    }
    else
    {
        self.backgroundColor = BG_COLOR_NORMAL;
    }
    [super setSelected:selected];
}

@end
