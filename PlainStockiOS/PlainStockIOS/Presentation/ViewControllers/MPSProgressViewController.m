#import "MPSProgressViewController.h"
#import "PlainStockiOS-Swift.h"
#import "BLController.h"
#import "DBManager.h"
#import "WSManager.h"
#import "AppDelegate.h"
#import "DataDateCash.h"
#import "WYPopoverController.h"
#import "MPSDateRangeViewController.h"


#define ONE_DAY_SECONDS 60*60*24;
#define ONE_DAY_MILIS 60*60*24*1000;
#define ONE_AND_HALF_DAY_MILIS 60*60*24*1000*1.5;
#define MAX_CHART_POINTS_DEFAULT 500;
#define TIMER_SLEEP 120.0
#define BTN_BG_COLOR_SELECTE

@interface MPSProgressViewController () <ChartViewDelegate, UIAlertViewDelegate, WYPopoverControllerDelegate, DateRangePopoverDelegate> {
    
    WYPopoverController* dateRangePopoverViewController;
}

@property (strong, nonatomic) UIDatePicker *timeLeapDatePicker;
@property (nonatomic) DateRange currentChartDateRange;
@property (nonatomic) NSInteger maxChartPoints;

@end

@implementation MPSProgressViewController
{
    NSTimer *timerChartUpdater;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    //To start and stop updater when app goes and comes from backround
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(receiveBackgroundNotification:)
                                                 name:UIApplicationWillResignActiveNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(receiveActiveNotification:)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
    
   
    
    //default range
    self.currentChartDateRange = oneDay;
    
    //max chart points
    NSDictionary *dicConfig = [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"]];
    self.maxChartPoints = [dicConfig objectForKey:@"MAX_CHART_POINTS"] != nil ? [[dicConfig objectForKey:@"MAX_CHART_POINTS"] integerValue] : MAX_CHART_POINTS_DEFAULT;
    
    [self setupRangePopoverAppearance];
    [self initChart];
    [self setupTimeLeapDatePicker];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    //sets strings for current language
    Language lang = [BLController sharedInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"Evolution" : @"Mi Evolución";
    self.balanceLabel.text = (lang == ENG) ? @"Balance: " : @"Saldo: ";
    self.rangeLabel.text = (lang == ENG) ? @"Range: " : @"Rango: ";
    [self.popoverButton setTitle:[self stringFromCurrentRange] forState:UIControlStateNormal];
    
    
    if ([BLController sharedInstance].isOnTimeMachineMode)
    {
        self.goToNextDateLabel.hidden = NO;
        self.dateJumpButton.hidden = NO;
        self.goToNextDateLabel.text = (lang == ENG) ? @"Go to the next date" : @"Ir a próxima fecha";
        self.botConstraint.constant = 8;
    }
    else
    {
        self.goToNextDateLabel.hidden = YES;
        self.dateJumpButton.hidden = YES;
        self.botConstraint.constant = -self.goToNextDateLabel.frame.size.height;
    }
    
    //let this view rotate to landscape
    AppDelegate* appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    appDelegate.restrictRotation = NO;
    
    //start updater
    [self startChartUpdaterTimer];
}


- (void)startChartUpdaterTimer
{
    if (timerChartUpdater == nil)
    {
        timerChartUpdater = [NSTimer scheduledTimerWithTimeInterval:TIMER_SLEEP target:self selector:@selector(fillChartAndUpdateView) userInfo:nil repeats:YES];
        [timerChartUpdater fire];
    }
}


- (void)stopChartUpdaterTimer
{
    [timerChartUpdater invalidate];
    timerChartUpdater = nil;
}

- (void)resetChartUpdaterTimer
{
    if ([timerChartUpdater isValid])
    {
        timerChartUpdater.fireDate = [NSDate dateWithTimeIntervalSinceNow:TIMER_SLEEP];
        [timerChartUpdater fire];
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
    //prevent other views from rotating
    AppDelegate* appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    appDelegate.restrictRotation = YES;
    
    //Stop updater
    [self stopChartUpdaterTimer];
}

- (void) receiveBackgroundNotification:(NSNotification*)notif
{
        [self stopChartUpdaterTimer];
}

- (void) receiveActiveNotification:(NSNotification*)notif
{
        [self startChartUpdaterTimer];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (void)initChart
{
    _chartView.delegate = self;
    
    // Setup chart style settings
    _chartView.descriptionText = @"";
    _chartView.noDataText = @"No data.";
    _chartView.highlightEnabled = NO;
    _chartView.dragEnabled = YES;
    [_chartView setScaleEnabled:YES];
    //_chartView.pinchZoomEnabled = YES;
    _chartView.doubleTapToZoomEnabled = NO;
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
    double iniDateMilis = [self getInitialDateForCurrentRange];
    NSMutableArray *balanceHistory = [[DBManager sharedInstance] getCashHistoryFrom:iniDateMilis to:DBL_MAX];
    NSMutableArray *chartBalanceHistory = [self getBalanceHistoryForChart:balanceHistory];
    
    NSMutableArray *xVals = [[NSMutableArray alloc] init];
    NSMutableArray *yVals = [[NSMutableArray alloc] init];
    
    NSDateFormatter *dbDateFormatter = [[NSDateFormatter alloc] init];
    NSDateFormatter *chartDateFormatter = [[NSDateFormatter alloc] init];
    [dbDateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    [chartDateFormatter setDateFormat:@"dd/MM/yy\nHH:mm"];
    NSDate *date;
    
    for (int i = 0; i < chartBalanceHistory.count; i++)
    {
        DataDateCash *ddc = chartBalanceHistory[i];
        date = [dbDateFormatter dateFromString: ddc.date];
        NSString *formattedDate = [chartDateFormatter stringFromDate:date];
        [xVals addObject:formattedDate];
        [yVals addObject:[[ChartDataEntry alloc] initWithValue:[ddc.cash doubleValue] xIndex:i]];
    }
    
    LineChartDataSet *chartDataSet = [[LineChartDataSet alloc] initWithYVals:yVals];
    chartDataSet.drawCubicEnabled = NO;
    chartDataSet.cubicIntensity = 0.2;
    chartDataSet.drawCirclesEnabled = NO;
    chartDataSet.fillAlpha = 0.9;
    chartDataSet.lineWidth = 3.0;
    chartDataSet.circleRadius = 5.0;
    [chartDataSet setColor:[UIColor colorWithRed:104/255.f green:241/255.f blue:175/255.f alpha:1.f]];
    chartDataSet.fillColor = [UIColor colorWithRed:153/255.f green:217/255.f blue:234/255.f alpha:0.9];
    chartDataSet.drawHorizontalHighlightIndicatorEnabled = NO;
    chartDataSet.drawVerticalHighlightIndicatorEnabled = NO;
    chartDataSet.drawFilledEnabled = YES;
    
    LineChartData *data = [[LineChartData alloc] initWithXVals:xVals dataSet:chartDataSet];
    [data setValueFont:[UIFont systemFontOfSize:9.f]];
    [data setDrawValues:NO];

    // Setup the chart emoticon
    NSUInteger count = balanceHistory.count;
    
    if (count < 2)
        [self.emoticonImageView setImage:[UIImage imageNamed:@"happy_face"]];
    else
    {
        double lastCash = [[[balanceHistory lastObject] cash] doubleValue];
        double nextToLastCash = [[[balanceHistory objectAtIndex:count - 2] cash] doubleValue];
        
        if (lastCash >= nextToLastCash)
            [self.emoticonImageView setImage:[UIImage imageNamed:@"happy_face"]];
        else
            [self.emoticonImageView setImage:[UIImage imageNamed:@"sad_face"]];
    }
    
    _chartView.data = data;
    
    [_chartView setNeedsDisplay];
    [_chartView animateWithXAxisDuration:0.8];
    
    
    //updates current balance label
    if (balanceHistory.count > 0)
        self.moneyLabel.text = [NSString stringWithFormat:@"$%.1f", [[[balanceHistory lastObject] cash] doubleValue]];
}


- (NSMutableArray *)getBalanceHistoryForChart:(NSMutableArray *)balanceHistory
{
    NSMutableArray *res = [[NSMutableArray alloc] init];
    
    if (self.maxChartPoints > balanceHistory.count)
        res = balanceHistory;
    else
    {
        int skipCount = ceil((float)balanceHistory.count / (float)self.maxChartPoints);
        int i;
        
        for (i = 0; i < balanceHistory.count; i += skipCount)
            [res addObject:balanceHistory[i]];
        
        if (i < balanceHistory.count)
            [res addObject: [balanceHistory lastObject]];
    }
    return res;
}

- (void)setupTimeLeapDatePicker
{
    
  // Picker style
    self.timeLeapDatePicker = [[UIDatePicker alloc] init];
    self.timeLeapDatePicker.datePickerMode = UIDatePickerModeDate;
    self.timeLeapDatePicker.backgroundColor =  [UIColor colorWithWhite:1.0 alpha:0.90];
    
  // Picker max, min and current dates
    NSTimeInterval oneDay = ONE_DAY_SECONDS;
    NSDate *minDate = [[BLController sharedInstance].timeMachineDate dateByAddingTimeInterval: oneDay];
    
    if ([minDate compare:[NSDate date]] != NSOrderedAscending) //(minDate >= current_date)
        minDate = [NSDate date];
    
    [self.timeLeapDatePicker setMaximumDate:[NSDate date]];
    [self.timeLeapDatePicker setMinimumDate:minDate];
    self.timeLeapDatePicker.date = minDate;
    
    
  // Toolbar setup
    UIToolbar *toolbar =[[UIToolbar alloc]initWithFrame:CGRectMake(0,0, self.view.frame.size.width,44)];
    toolbar.barStyle = UIBarStyleDefault;
    [toolbar setTranslucent:YES];
    toolbar.alpha = 0.9;
    
    UIBarButtonItem *cancelButton =[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel
                                                                               target:self
                                                                               action:@selector(cancelDateSelection)];
    
    UIBarButtonItem *flexibleSpace =[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                                                 target:self
                                                                                 action:nil];
    
    UIBarButtonItem *doneButton =[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone
                                                                               target:self
                                                                               action:@selector(doneSelectingDate)];
    
    [toolbar setItems:@[cancelButton, flexibleSpace, doneButton]];
    
    
    [self.auxDateTextField setInputView:self.timeLeapDatePicker];
    [self.auxDateTextField setInputAccessoryView:toolbar];
}


- (BOOL)isSameDay:(NSDate*)date1 otherDay:(NSDate*)date2 {
    NSCalendar* calendar = [NSCalendar currentCalendar];
    
    unsigned unitFlags = NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay;
    NSDateComponents* comp1 = [calendar components:unitFlags fromDate:date1];
    NSDateComponents* comp2 = [calendar components:unitFlags fromDate:date2];
    
    return [comp1 day]   == [comp2 day] &&
    [comp1 month] == [comp2 month] &&
    [comp1 year]  == [comp2 year];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Actions

- (IBAction)showTimeLeapDatePicker:(id)sender
{
    [self.auxDateTextField becomeFirstResponder];
    
    
}

- (void)doneSelectingDate
{
    //Ask the user for confirmation before jumping forward in time
    Language lang = [BLController sharedInstance].language;
    NSString *title = (lang == ENG) ? @"Message" : @"Mensaje";
    NSString *cancel = (lang == ENG) ? @"No" : @"No";
    NSString *ok = (lang == ENG) ? @"Yes" : @"Si";
    NSString *msg = (lang == ENG) ? @"Jump on time? " : @"Saltar en el timepo?";
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:cancel
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:ok];
    alert.tag = 1;
    [alert show];
    
    [self.view endEditing:YES];
}


- (void)cancelDateSelection
{
    [self.view endEditing:YES];
}


- (void)jumpForwardInTime
{
    NSLog(@"jumpForwardInTime");
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *jumpDateStr = [dateFormatter stringFromDate:self.timeLeapDatePicker.date];
    WSResponse *jumpDateResponse = [[WSManager sharedInstance] timeJumpToDateSynchronous:jumpDateStr];
    
    if (jumpDateResponse.error == nil)
    {
        [[BLController sharedInstance] setTimeMachineDate:self.timeLeapDatePicker.date];
        [self.timeLeapDatePicker setMinimumDate:self.timeLeapDatePicker.date];
        
        if ([self isSameDay:self.timeLeapDatePicker.minimumDate otherDay:self.timeLeapDatePicker.maximumDate])
        {
            //Tell the user he has reached the end of time machine mode
            Language lang = [BLController sharedInstance].language;
            NSString *title = (lang == ENG) ? @"Message" : @"Mensaje";
            NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
            NSString *msg = (lang == ENG) ? @"Reached the end of simulation, now game will continue on actual date." : @"Has llegado al fin de la simulación, ahora el juego seguira en la fecha real.";
            UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                             message:msg
                                                            delegate:self
                                                   cancelButtonTitle:ok
                                                   otherButtonTitles: nil];
            alert.tag = 0;
            [alert show];
            
            
            //Reached the end of simulation, now game continues from actual date
            [[BLController sharedInstance] setIsOnTimeMachineMode:NO];
            [self viewWillAppear:NO];
        }
        else
        {
            [self resetChartUpdaterTimer]; //updates view and resets timer
        }
        
    }
    else
    {
        [[BLController sharedInstance] showConnectionErrorAlert];
    }
}


#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 1 && buttonIndex == 1)
    {
        [self jumpForwardInTime];
    }
}


- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)orientation
                                duration:(NSTimeInterval)duration
{
    if (orientation == UIInterfaceOrientationLandscapeLeft || orientation == UIInterfaceOrientationLandscapeRight)
    {
        [self.view endEditing:YES];
        [dateRangePopoverViewController dismissPopoverAnimated:YES];
        [self.tabBarController.tabBar setHidden:YES];
        self.navBarTopConstraint.constant = - self.navigationBar.bounds.size.height;
        self.botConstraint.constant = - (self.goToNextDateLabel.frame.origin.y + self.dateJumpButton.frame.size.height - self.rangeLabel.frame.origin.y - 8);
    }
    else
    {
        [self.tabBarController.tabBar setHidden:NO];
        self.navBarTopConstraint.constant = 0;
        
        if ([BLController sharedInstance].isOnTimeMachineMode)
            self.botConstraint.constant = 8;
        else
            self.botConstraint.constant = -self.goToNextDateLabel.frame.size.height;
    }
}


#pragma mark - Popover Delegate

- (void)setupRangePopoverAppearance
{
    UIColor *blueColor = [UIColor colorWithRed:153/255.f green:217/255.f blue:234/255.f alpha:1.0];
    UIColor *greenColor = [UIColor colorWithRed:104/255.f green:241/255.f blue:175/255.f alpha:0.5];
    UIColor *clearColor = [UIColor clearColor];
    
    //general config
    [WYPopoverController setDefaultTheme:[WYPopoverTheme themeForIOS7]];
    
    WYPopoverBackgroundView *popoverAppearance = [WYPopoverBackgroundView appearance];

    //border
    [popoverAppearance setOuterCornerRadius:6];
    [popoverAppearance setInnerCornerRadius:2];
    [popoverAppearance setBorderWidth:8];
    
    //arrow
    [popoverAppearance setArrowHeight:20];
    [popoverAppearance setArrowBase:30];
    
    //shadow
    [popoverAppearance setOuterShadowBlurRadius:8];
    [popoverAppearance setInnerShadowBlurRadius:8];
    [popoverAppearance setOuterShadowOffset:CGSizeMake(0, 0)];
    [popoverAppearance setInnerShadowOffset:CGSizeMake(0, 0)];
    [popoverAppearance setGlossShadowOffset:CGSizeMake(0, 0)];
    
    //color
    [popoverAppearance setFillTopColor:blueColor];
    [popoverAppearance setFillBottomColor:greenColor];
    
    [popoverAppearance setOuterStrokeColor:clearColor];
    [popoverAppearance setInnerStrokeColor:clearColor];
    
    [popoverAppearance setOuterShadowColor:blueColor];
    [popoverAppearance setInnerShadowColor:blueColor];
    [popoverAppearance setGlossShadowColor:blueColor];
    
    //Creation
    
    MPSDateRangeViewController *dateRangeViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"WYSettingsViewController"];
    dateRangeViewController.preferredContentSize = CGSizeMake(160, 180);
    dateRangeViewController.modalInPopover = NO; //so clicking outside dismisses popover
    dateRangeViewController.delegate = self;
    
    dateRangePopoverViewController = [[WYPopoverController alloc] initWithContentViewController:dateRangeViewController];
    dateRangePopoverViewController.delegate = self;
    
    UIView *popBtn = (UIView *)self.popoverButton;
    dateRangePopoverViewController.passthroughViews = @[popBtn];
    
}


- (IBAction)popoverBtnPressed:(id)sender
{
    if ([dateRangePopoverViewController isPopoverVisible])
    {
        [dateRangePopoverViewController dismissPopoverAnimated:YES];
    }
    else
    {
        [dateRangePopoverViewController presentPopoverFromRect:self.popoverButton.bounds inView:self.popoverButton permittedArrowDirections:WYPopoverArrowDirectionAny animated:YES options:WYPopoverAnimationOptionFadeWithScale];
    }
}


- (NSString *)stringFromCurrentRange
{
    NSString * res;
    Language l = ([BLController sharedInstance].language == ENG) ? ENG : SPA;
    
    switch (self.currentChartDateRange) {
        case oneDay:
            res = l == ENG ? @"One day" : @"Un día";
            break;
        case oneWeek:
            res = l == ENG ? @"One week" : @"Una semana";
            break;
        case twoWeeks:
            res = l == ENG ? @"Two weeks" : @"Dos semanas";
            break;
        case oneMonth:
            res = l == ENG ? @"One month" : @"Un mes";
            break;
        case sixMonths:
            res = l == ENG ? @"Six months" : @"Seis meses";
            break;
        case oneYear:
            res = l == ENG ? @"One year" : @"Un año";
            break;
        case fiveYears:
            res = l == ENG ? @"Five years" : @"Cinco años";
            break;
        case All:
            res = l == ENG ? @"All" : @"Todo";
            break;
        default:
            res = @"?";
            break;
    }
    return res;
}

- (double)getInitialDateForCurrentRange
{
    double res;
    
    switch (self.currentChartDateRange) {
        case oneDay:
        {
            res = [BLController sharedInstance].isOnTimeMachineMode ? 60*60*24*1000*1.5 : ONE_DAY_MILIS; //cause 1-day granularity on time machine
            break;
        }
        case oneWeek:
            res = 7.0 * ONE_DAY_MILIS;
            break;
        case twoWeeks:
            res = 14.0 * ONE_DAY_MILIS;
            break;
        case oneMonth:
            res = 30.0 * ONE_DAY_MILIS;
            break;
        case sixMonths:
            res = 30.0 * 6.0 * ONE_DAY_MILIS;
            break;
        case oneYear:
            res = 365.0 * ONE_DAY_MILIS;
            break;
        case fiveYears:
            res = 365.0 * 5.0 * ONE_DAY_MILIS;
            break;
        case All:
            res = 0.0;
            break;
        default:
            res = 0.0;
            break;
    }
    
    if (res != 0)
    {
        if ([BLController sharedInstance].isOnTimeMachineMode)
        {
            res = [[BLController sharedInstance].timeMachineDate timeIntervalSince1970]*1000 - res;
            NSLog(@"timeMachineDate = %@", [BLController sharedInstance].timeMachineDate);
        }
        else
        {
            res = [BLController sharedInstance].lastDatePersisted - res;
            NSLog(@"lastDatePersisted = %f", [BLController sharedInstance].lastDatePersisted);
        }
    }
    
    
    return res;
}



-(void) DateRangePopoverSelectedRange:(NSInteger)selectedRange
{
    self.currentChartDateRange = selectedRange;
    [self.popoverButton setTitle:[self stringFromCurrentRange] forState:UIControlStateNormal];
    [dateRangePopoverViewController dismissPopoverAnimated:YES];
    [self resetChartUpdaterTimer];
    [self.chartView fitScreen]; //resets to initial zoom
}


@end

