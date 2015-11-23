//
//  MPSProgressViewController.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/14/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSProgressViewController.h"
#import "PlainStockiOS-Swift.h"

@interface MPSProgressViewController () <ChartViewDelegate, UIAlertViewDelegate>

@property (weak, nonatomic) IBOutlet LineChartView *chartView;
@property (strong, nonatomic) NSDate* gameDate;
@property (strong, nonatomic) UIDatePicker *timeLeapDatePicker;
@property (weak, nonatomic) IBOutlet UITextField *auxDateTextField;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *botConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *navBarTopConstraint;
@property (weak, nonatomic) IBOutlet UINavigationBar *navigationBar;

@end

@implementation MPSProgressViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupChart];
    [self setupTimeLeapDatePicker];
    [self setupMoneyLabel];
}


- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    [self setupChart];
    [self setupTimeLeapDatePicker];
    [self setupMoneyLabel];
}


- (void)setupChart
{
    _chartView.delegate = self;
    
  // Setup chart style settings
    _chartView.descriptionText = @"";
    _chartView.noDataTextDescription = @"You need to provide data for the chart.";
    _chartView.highlightEnabled = NO;
    _chartView.dragEnabled = YES;
    [_chartView setScaleEnabled:YES];
    _chartView.pinchZoomEnabled = YES;
    _chartView.drawGridBackgroundEnabled = NO;
    
    _chartView.leftAxis.drawAxisLineEnabled = NO;
    _chartView.leftAxis.drawGridLinesEnabled = YES;
    _chartView.leftAxis.valueFormatter = [[NSNumberFormatter alloc] init];
    _chartView.leftAxis.valueFormatter.maximumFractionDigits = 0;
    _chartView.leftAxis.valueFormatter.negativeSuffix = @" $";
    _chartView.leftAxis.valueFormatter.positiveSuffix = @" $";
    _chartView.leftAxis.labelPosition = YAxisLabelPositionOutsideChart;
    _chartView.leftAxis.spaceTop = 0.15;
    
    _chartView.xAxis.drawAxisLineEnabled = NO;
    _chartView.xAxis.drawGridLinesEnabled = NO;
    _chartView.xAxis.avoidFirstLastClippingEnabled = YES;
    
    _chartView.rightAxis.enabled = NO;
    _chartView.legend.enabled = NO;
    
    
  // Set the chart data
    [self setDataCount:10.0 range:10.0];
    
    
  // Setup the chart baloon marker
     BalloonMarker *marker = [[BalloonMarker alloc] initWithColor:[UIColor colorWithWhite:180/255. alpha:0.5] font:[UIFont systemFontOfSize:12.0] insets: UIEdgeInsetsMake(8.0, 8.0, 20.0, 8.0)];
     marker.minimumSize = CGSizeMake(80.f, 40.f);
     _chartView.marker = marker;

    
  // Fill the chart under the line
    LineChartDataSet *chartDataSet = _chartView.data.dataSets[0];
    chartDataSet.drawFilledEnabled = YES;
    
    
  // Setup the chart face icon
    NSUInteger chartValuesCount = chartDataSet.yVals.count;
        
    if ( (chartValuesCount == 1) || ((chartValuesCount > 1)
         && ([chartDataSet yValForXIndex:chartValuesCount - 1] >= [chartDataSet yValForXIndex:chartValuesCount - 2])) )
    {
        [self.emoticonImageView setImage:[UIImage imageNamed:@"happy_face"]];
    }
    else
    {
        [self.emoticonImageView setImage:[UIImage imageNamed:@"sad_face"]];
    }
    

  // Add the chart to the view
    [_chartView setNeedsDisplay];
    [_chartView animateWithXAxisDuration:2.0 yAxisDuration:2.0];
}



- (void)setupTimeLeapDatePicker
{
    
  // Picker style
    self.timeLeapDatePicker = [[UIDatePicker alloc] init];
    self.timeLeapDatePicker.datePickerMode = UIDatePickerModeDate;
    self.timeLeapDatePicker.backgroundColor =  [UIColor colorWithWhite:1.0 alpha:0.90];
    
    
  // Picker max and min dates
    NSString *str =@"01/01/1950";
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"MM/dd/yyyy"];
    NSDate *minDate = [formatter dateFromString:str];

    [self.timeLeapDatePicker setMaximumDate:[NSDate date]];
    [self.timeLeapDatePicker setMinimumDate:minDate];
    
    
    
  // Toolbar setup
    UIToolbar *toolbar =[[UIToolbar alloc]initWithFrame:CGRectMake(0,0, self.view.frame.size.width,44)];
    toolbar.barStyle = UIBarStyleDefault;
    [toolbar setTranslucent:YES];
    toolbar.alpha = 0.9;
    
    UIBarButtonItem *flexibleSpace =[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                                                 target:self
                                                                                 action:nil];
    
    UIBarButtonItem *doneButton =[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone
                                                                               target:self
                                                                               action:@selector(goToNextDate:)];
    
    [toolbar setItems:@[flexibleSpace, doneButton]];
    
    [self.auxDateTextField setInputView:self.timeLeapDatePicker];
    [self.auxDateTextField setInputAccessoryView:toolbar];
}



- (void)setDataCount:(int)count range:(double)range
{
    NSMutableArray *xVals = [[NSMutableArray alloc] init];
    
    for (int i = 0; i < count; i++)
    {
        [xVals addObject:[@(i + 1998) stringValue]];
    }
    
    NSMutableArray *yVals1 = [[NSMutableArray alloc] init];
    
    for (int i = 0; i < count; i++)
    {
        double mult = (range + 1);
        double val = (double) (arc4random_uniform(mult)) + 20;
        [yVals1 addObject:[[ChartDataEntry alloc] initWithValue:val xIndex:i]];
    }
    
    LineChartDataSet *cashDataSet = [[LineChartDataSet alloc] initWithYVals:yVals1];
    cashDataSet.drawCubicEnabled = YES;
    cashDataSet.cubicIntensity = 0.2;
    cashDataSet.drawCirclesEnabled = NO;
    cashDataSet.fillAlpha = 1.0; //TODO: hace el relleno totalmente opaco, verificar
    cashDataSet.lineWidth = 2.0;
    cashDataSet.circleRadius = 5.0;
    //cashDataSet.highlightColor = [UIColor colorWithRed:244/255.f green:117/255.f blue:117/255.f alpha:1.f]; //Por el momento no esta decidido si se utilizara indicador vertical
    [cashDataSet setColor:[UIColor colorWithRed:104/255.f green:241/255.f blue:175/255.f alpha:1.f]];
    cashDataSet.fillColor = [UIColor colorWithRed:153/255.f green:217/255.f blue:234/255.f alpha:1.f];
    cashDataSet.drawHorizontalHighlightIndicatorEnabled = NO;
    cashDataSet.drawVerticalHighlightIndicatorEnabled = NO;
    
    LineChartData *data = [[LineChartData alloc] initWithXVals:xVals dataSet:cashDataSet];
    [data setValueFont:[UIFont systemFontOfSize:9.f]];
    [data setDrawValues:NO];
    
    _chartView.data = data;
    

}




- (void)setupMoneyLabel
{
    LineChartDataSet *chartDataSet = _chartView.data.dataSets[0];
    NSString* moneyString = [NSString stringWithFormat:@"$ %.2f", [chartDataSet yValForXIndex:chartDataSet.yVals.count - 1]];
    self.moneyLabel.text = moneyString;
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Actions

- (IBAction)showNextEventAlert:(id)sender
{
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:@"Mensaje"
                                                     message:@"Seguro que desea avanzar el tiempo?"
                                                    delegate:self
                                           cancelButtonTitle:@"Cancelar"
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:@"Aceptar"];
    [alert show];
}


- (IBAction)showTimeLeapDatePicker:(id)sender
{
    [self.auxDateTextField becomeFirstResponder];
    
    
}

- (void)goToNextEvent
{
    NSLog(@"goToNextEvent");
    
}

- (void)goToNextDate:(id)sender
{
    NSLog(@"goToNextDate");
    [self.view endEditing:YES];
    
}



#pragma mark - ChartViewDelegate

- (void)chartValueSelected:(ChartViewBase * __nonnull)chartView entry:(ChartDataEntry * __nonnull)entry dataSetIndex:(NSInteger)dataSetIndex highlight:(ChartHighlight * __nonnull)highlight
{

    
}


- (void)chartValueNothingSelected:(ChartViewBase * __nonnull)chartView
{

    
}


#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1)
    {
        [self goToNextEvent];
    }
}


- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)orientation
                                duration:(NSTimeInterval)duration
{
    if (orientation == UIInterfaceOrientationLandscapeLeft || orientation == UIInterfaceOrientationLandscapeRight)
    {

        [self.tabBarController.tabBar setHidden:YES];
        self.botConstraint.constant = - (self.dateJumpButton.frame.origin.y + self.dateJumpButton.frame.size.height - self.moneyLabel.frame.origin.y);
        self.navBarTopConstraint.constant = - self.navigationBar.bounds.size.height;
    }
    else
    {
        [self.tabBarController.tabBar setHidden:NO];
        self.botConstraint.constant = 33;
        self.navBarTopConstraint.constant = 0;
        
    }
}


@end

