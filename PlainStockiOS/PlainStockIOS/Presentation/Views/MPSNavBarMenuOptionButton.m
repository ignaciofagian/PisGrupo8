#import "MPSNavBarMenuOptionButton.h"



@implementation MPSNavBarMenuOptionButton

-(void) setup
{
    //sets color and border
    [self setBackgroundColor:[UIColor colorWithRed:0.99 green:0.99 blue:0.99 alpha:1.0]];
    [[self layer] setBorderWidth:0.9f];
    [[self layer] setBorderColor:[UIColor colorWithRed:0.90 green:0.90 blue:0.90 alpha:0.9].CGColor];
}

-(void) setHighlighted:(BOOL)highlighted {

    if(highlighted) {
        self.backgroundColor = [UIColor colorWithRed:0.80 green:0.80 blue:0.80 alpha:1.0];
    } else {
        self.backgroundColor = [UIColor colorWithRed:0.99 green:0.99 blue:0.99 alpha:1.0];
    }
    [super setHighlighted:highlighted];
}

-(void) setSelected:(BOOL)selected {
    
    if(selected) {
        self.backgroundColor = [UIColor colorWithRed:0.80 green:0.80 blue:0.80 alpha:1.0];
    } else {
        self.backgroundColor = [UIColor colorWithRed:0.99 green:0.99 blue:0.99 alpha:1.0];
    }
    [super setSelected:selected];
}

@end
