//
//  MPSProgressViewController.m
//  PlainStockiOS
//
//  Created by Bruno Rodao on 9/14/15.
//  Copyright (c) 2015 FING. All rights reserved.
//

#import "MPSProgressViewController.h"
#import "PlainStockiOS-Swift.h"
#import "BLController.h"
#import "AppDelegate.h"
#import "DataDateCash.h"

@interface MPSProgressViewController () <ChartViewDelegate, UIAlertViewDelegate>

@property (strong, nonatomic) NSDate* gameDate;
@property (strong, nonatomic) UIDatePicker *timeLeapDatePicker;
@property (strong, nonatomic) NSMutableArray *balanceHistory;

@end

@implementation MPSProgressViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    [BLController getInstance].progressDelegate = self;
    [self initChart];
    [self setupTimeLeapDatePicker];
}

- (void)viewWillAppear:(BOOL)animated
{
    //refresh chart and balance label async
    [self fillChartAndUpdateView];
    
    //let this view rotate to landscape
    AppDelegate* appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    appDelegate.restrictRotation = NO;
    
    //sets strings for current language
    Language lang = [BLController getInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"Evolution" : @"Mi Evolución";
    self.balanceLabel.text = (lang == ENG) ? @"Balance: " : @"Saldo: ";
    self.goToNextEventLabel.text = (lang == ENG) ? @"Go to the next event" : @"Ir a próximo evento";
    self.goToNextDateLabel.text = (lang == ENG) ? @"Go to the next date" : @"Ir a próxima fecha";
}

- (void)viewWillDisappear:(BOOL)animated
{
    //prevent other views from rotating
    AppDelegate* appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    appDelegate.restrictRotation = YES;
}

- (void)initChart
{
    _chartView.delegate = self;
    
    // Setup chart style settings
    _chartView.descriptionText = @"";
    _chartView.noDataText = @"Loading...";
    _chartView.highlightEnabled = NO;
    _chartView.dragEnabled = YES;
    [_chartView setScaleEnabled:YES];
    //_chartView.pinchZoomEnabled = YES;
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
    //[_chartView.xAxis setLabelFont:[UIFont fontWithName:@"Verdana" size:8]];
    [_chartView.xAxis setLabelFont:[UIFont fontWithName:_chartView.xAxis.labelFont.fontName size:9]];
    _chartView.rightAxis.enabled = NO;
    _chartView.legend.enabled = NO;
    
    // Setup the chart baloon marker
     BalloonMarker *marker = [[BalloonMarker alloc] initWithColor:[UIColor colorWithWhite:180/255. alpha:0.5] font:[UIFont systemFontOfSize:12.0] insets: UIEdgeInsetsMake(8.0, 8.0, 20.0, 8.0)];
     marker.minimumSize = CGSizeMake(80.f, 40.f);
     _chartView.marker = marker;
}


- (void) fillChartAndUpdateView
{
    //TODO: Actualmente pide todo al server (TODO), debe pedir a BD un rango a mostrar y falta toda la logica asociada a los rangos
    //gets balance history from server and updates the chart via delegate call when data is received
    [[BLController getInstance] getCashHistoryFrom:@"1950-01-01" To:@"2020-01-01" Delegate:self]; //TODO: cambiar con fecha bien y con llamada a db
}



#pragma mark - BLController delegate

-(void)receivedDateCashHistoryResponse:(WSResponse *)response
{
    if (response.error == nil)
    {
        //updates the chart
        NSMutableArray *balanceHistory = [response.data mutableCopy];
        NSMutableArray *xVals = [[NSMutableArray alloc] init];
        NSMutableArray *yVals = [[NSMutableArray alloc] init];
        
        NSDateFormatter *dbDateFormatter = [[NSDateFormatter alloc] init];
        NSDateFormatter *chartDateFormatter = [[NSDateFormatter alloc] init];
        [dbDateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
        [chartDateFormatter setDateFormat:@"MM/dd/yyyy\nHH:mm"];
        
        
        NSDate *date;
        
        for (int i = 0; i < balanceHistory.count; i++)
        {
            DataDateCash *ddc = balanceHistory[i];
            date = [dbDateFormatter dateFromString: ddc.date];
            NSString *formattedDate = [chartDateFormatter stringFromDate:date];
            [xVals addObject:formattedDate];
            [yVals addObject:[[ChartDataEntry alloc] initWithValue:[ddc.cash longLongValue] xIndex:i]];
        }
        
        LineChartDataSet *chartDataSet = [[LineChartDataSet alloc] initWithYVals:yVals];
        chartDataSet.drawCubicEnabled = YES;
        chartDataSet.cubicIntensity = 0.2;
        chartDataSet.drawCirclesEnabled = NO;
        chartDataSet.fillAlpha = 1.0;
        chartDataSet.lineWidth = 2.0;
        chartDataSet.circleRadius = 5.0;
        [chartDataSet setColor:[UIColor colorWithRed:104/255.f green:241/255.f blue:175/255.f alpha:1.f]];
        chartDataSet.fillColor = [UIColor colorWithRed:153/255.f green:217/255.f blue:234/255.f alpha:1.f];
        chartDataSet.drawHorizontalHighlightIndicatorEnabled = NO;
        chartDataSet.drawVerticalHighlightIndicatorEnabled = NO;
        chartDataSet.drawFilledEnabled = YES;
        
        LineChartData *data = [[LineChartData alloc] initWithXVals:xVals dataSet:chartDataSet];
        [data setValueFont:[UIFont systemFontOfSize:9.f]];
        [data setDrawValues:NO];
        
        // Setup the chart emoticon
        NSUInteger yCount = yVals.count;
        
        if ((yCount == 1) || ((yCount > 1) && ([chartDataSet yValForXIndex:yCount - 1] >= [chartDataSet yValForXIndex:yCount - 2])))
            [self.emoticonImageView setImage:[UIImage imageNamed:@"happy_face"]];
        else
            [self.emoticonImageView setImage:[UIImage imageNamed:@"sad_face"]];
        
        _chartView.data = data;
        
        [_chartView setNeedsDisplay];
        //[_chartView animateWithXAxisDuration:2.0 yAxisDuration:2.0];
        [_chartView animateWithXAxisDuration:0.8];
        
        
        //updates current balance label
        self.moneyLabel.text = [NSString stringWithFormat:@"$ %.2f", [chartDataSet yValForXIndex:yCount - 1]];
        
    }
    else
    {
        UIAlertView *theAlert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                           message:response.error
                                                          delegate:self
                                                 cancelButtonTitle:@"OK"
                                                 otherButtonTitles:nil];
        [theAlert show];
    }
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



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Actions

- (IBAction)showNextEventAlert:(id)sender
{
    Language lang = [BLController getInstance].language;
    NSString *title = (lang == ENG) ? @"Message" : @"Mensaje";
    NSString *cancel = (lang == ENG) ? @"Cancel" : @"Cancelar";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Sure you want to move the time forward?" : @"¿Seguro que desea avanzar el tiempo?";
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:cancel
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:ok];
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

