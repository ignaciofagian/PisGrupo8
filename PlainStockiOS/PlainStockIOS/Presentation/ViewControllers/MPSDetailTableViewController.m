#import "MPSDetailTableViewController.h"
#import "MPSDetailTableViewCell.h"
#import "BLController.h"
#import "DataDateCash.h"
#import "DBManager.h"

@interface MPSDetailTableViewController ()

@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;

@end


static NSString *cellIden = @"cellIden";

@implementation MPSDetailTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [BLController getInstance].detailsDelegate = self;
    [[BLController getInstance] getCashHistoryFrom:@"2015-10-01" To:@"2015-10-17" Delegate:self];
}

- (void)viewWillAppear:(BOOL)animated
{
    Language lang = [BLController getInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"Details" : @"Detalles";
    self.balanceLabel.text = (lang == ENG) ? @"Balance" : @"Saldo";
    self.momentLabel.text = (lang == ENG) ? @"Moment" : @"Momento";
}

-(void)receivedDateCashHistoryResponse:(WSResponse *)response
{
    NSArray *cashHistory = [response getData];
    NSString *error = [response getError];
    
    if (error == nil){
        self.balanceHistory = [NSMutableArray array];
        NSInteger length = [cashHistory count];
        DataDateCash *first_balance = [cashHistory objectAtIndex:0];
        NSString *lastvaluestring = first_balance.cash;
        long lastvalue = [lastvaluestring longLongValue];
        
        
        for (int indexValue = 0; indexValue< length; indexValue++)
        {
            DataDateCash *balance = [cashHistory objectAtIndex:indexValue];
        
            NSString *date = balance.date;
            NSString *currentValue = balance.cash;
            long long_current_value = [currentValue longLongValue];
            
            date = [date substringToIndex:10]; //date is of format 2015/01/01, 10 characaters, time is removed
        
            //TODO MAURI: REVISAR LOS PORCENTAJES PORQUE SE ESTA TRUNCANDO MUCHO
            long porcentaje = -1*((lastvalue- long_current_value)*100)/long_current_value;
            lastvalue = long_current_value;
            NSString *porcentajeString = [NSString stringWithFormat:@"%ld", porcentaje];
            porcentajeString = [porcentajeString stringByAppendingString:@"%"];
            
            NSDictionary *balance2 = @{
                                       @"date": date,
                                       @"percentage": porcentajeString,
                                       @"mount": currentValue
                                       };
            [self.balanceHistory addObject:balance2];
        }
        [_tableView reloadData];
    }
    else{
        //tirar una alerta y decidir que hacer aca
        //TODO MAURI: COPIARLE A ANDROID EL COMPORTAMIENTO DSPS DE MOSTRADO LA ALERTA
        UIAlertView *theAlert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                           message:error
                                                          delegate:self
                                                 cancelButtonTitle:@"OK"
                                                 otherButtonTitles:nil];
        [theAlert show];
    }
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
    {
        cell = [[MPSDetailTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIden];
    }
    
    [cell.lblDate setText:[[self.balanceHistory objectAtIndex:indexPath.row] objectForKey:@"date"]];
    [cell.lblPercentage setText:[[self.balanceHistory objectAtIndex:indexPath.row] objectForKey:@"percentage"]];
    
    if ([cell.lblPercentage.text rangeOfString:@"-"].location == NSNotFound) {
        cell.lblPercentage.textColor=[UIColor greenColor];
    } else {
        cell.lblPercentage.textColor=[UIColor redColor];;
    }
    
    
    [cell.lblMount setText:[[self.balanceHistory objectAtIndex:indexPath.row] objectForKey:@"mount"]];
    return cell;
    
}

@end
