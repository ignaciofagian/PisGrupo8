#import "MPSDetailTableViewController.h"
#import "MPSDetailTableViewCell.h"
#import "BLController.h"
#import "WSManager.h"
#import "DataDateCash.h"
#import "DBManager.h"

@interface MPSDetailTableViewController ()

@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;
@property (strong, nonatomic) NSMutableArray  *balanceHistory;
@property (strong, nonatomic) NSDateFormatter *dbDateFormatter;
@property (strong, nonatomic) NSDateFormatter *tableDateFormatter;

@end


static NSString *cellIden = @"cellIden";

@implementation MPSDetailTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.dbDateFormatter = [[NSDateFormatter alloc] init];
    self.tableDateFormatter = [[NSDateFormatter alloc] init];
    [self.dbDateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    [self.tableDateFormatter setDateFormat:@"dd/MM/yy - HH:mm"];
}

- (void)viewWillAppear:(BOOL)animated
{
    //sets strings for current language
    Language lang = [BLController sharedInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"Details" : @"Detalles";
    self.balanceLabel.text = (lang == ENG) ? @"Balance" : @"Saldo";
    self.momentLabel.text = (lang == ENG) ? @"Moment" : @"Momento";
    
    //refresh table
    [self getDataAndRefreshTable];
}


- (void)getDataAndRefreshTable
{
    if (self.balanceHistory == nil)
        self.balanceHistory = [[NSMutableArray alloc] init];
    
    //gets all balance history but add row only on change of balance
    NSMutableArray *newBalanceHistory = [[DBManager sharedInstance] getCashHistoryFrom:0 to:DBL_MAX];
    
    if (newBalanceHistory.count > 0)
    {
        [self.balanceHistory removeAllObjects];
        [self.balanceHistory addObject:newBalanceHistory[0]];
        
        for (int i = 1; i < newBalanceHistory.count; i++)
        {
            DataDateCash *ddc1 = newBalanceHistory[i-1];
            DataDateCash *ddc2 = newBalanceHistory[i];
            
            if (![ddc2.cash isEqualToString:ddc1.cash])
                [self.balanceHistory addObject:newBalanceHistory[i]];
        }
    }
         
    [self.tableView reloadData];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.balanceHistory count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    MPSDetailTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIden];
    if (cell == nil)
        cell = [[MPSDetailTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIden];
    
    DataDateCash *ddc_current = [self.balanceHistory objectAtIndex:indexPath.row];
    
    if (indexPath.row == 0)
    {
        [cell.lblDate setText:[self tableDateFromDBDateString:ddc_current.date]];
        [cell.lblPercentage setText:@"0.0%"];
        cell.lblPercentage.textColor = [UIColor greenColor];
        [cell.lblMount setText:[NSString stringWithFormat:@"$%@",ddc_current.cash]];
    }
    else
    {
        DataDateCash *ddc_last = [self.balanceHistory objectAtIndex:indexPath.row - 1];
        DataDateCash *ddc_current = [self.balanceHistory objectAtIndex:indexPath.row];
        
        NSString *lastvaluestring = ddc_last.cash;
        NSString *currentValueString = ddc_current.cash;
        
        float lastvalue = [lastvaluestring floatValue];
        float currentValue = [currentValueString floatValue];
        
        //percentage = (current - last) * 100 / last
        float percentage = (currentValue - lastvalue) * 100.0 / lastvalue;
        
        NSString *percentageString = [NSString stringWithFormat:@"%.1f", percentage];
        percentageString = [percentageString stringByAppendingString:@"%"];
        
        if (percentage >= 0.0)
        {
            cell.lblPercentage.textColor = [UIColor greenColor];
            [cell.lblPercentage setText:[NSString stringWithFormat:@"+%@", percentageString]];
        }
        else
        {
            cell.lblPercentage.textColor = [UIColor redColor];
            [cell.lblPercentage setText:[NSString stringWithFormat:@"%@", percentageString]];
        }
        
        cell.lblPercentage.textAlignment = NSTextAlignmentCenter;
        
        [cell.lblDate setText:[self tableDateFromDBDateString:ddc_current.date]];
        [cell.lblMount setText:[NSString stringWithFormat:@"$%@",ddc_current.cash]];
    }
    
    return cell;
}


- (NSString *)tableDateFromDBDateString:(NSString *)dbDateString
{
    NSDate *date = [self.dbDateFormatter dateFromString: dbDateString];
    NSString *tableFormattedDate = [self.tableDateFormatter stringFromDate:date];
    return tableFormattedDate;
}

@end
