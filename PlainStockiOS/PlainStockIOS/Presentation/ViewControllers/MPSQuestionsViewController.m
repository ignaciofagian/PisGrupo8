#import "MPSQuestionsViewController.h"
#import "MPSQuestionsTableViewCell.h"
#import "BLController.h"
#import "WSManager.h"
#import "DataQuestion.h"

//Constants
#define BTN_BG_COLOR_NORMAL [UIColor colorWithRed:239.0/255.0 green:239.0/255.0 blue:244.0/255.0 alpha:1.0]
#define BTN_BG_COLOR_SELECTED [UIColor colorWithRed:0.0 green:128.0/255.0 blue:1.0 alpha:1.0]
#define BTN_TITLE_COLOR_NORMAL [UIColor colorWithRed:0.0 green:128.0/255.0 blue:1.0 alpha:1.0]
#define BTN_TITLE_COLOR_SELECTED [UIColor whiteColor]

double const ANSWER_BUTTON_HEIGHT = 30.0;
double const ANSWER_BUTTON_WIDTH = 180.0;
static NSString *cellIden = @"answerCellIdent";


@interface MPSQuestionsViewController () <UIAlertViewDelegate>

@property (nonatomic) BOOL firstTime;
@property (nonatomic) NSInteger currentQuestionIndex;
@property (nonatomic) NSInteger lastAnsweredQuestion;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableViewHeightConstraint;
@property (weak, nonatomic) IBOutlet UINavigationItem *navBarTitle;

@end

@implementation MPSQuestionsViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.firstTime = YES;
    [WSManager sharedInstance].questionsDelegate = self;
    self.lastAnsweredQuestion = self.lastConfirmedAnswer;
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    [self.navBarMenuOptionButton setup]; //nav bar menu style
    [self updateCurrentQuestionIndex];
}

- (void)viewWillAppear:(BOOL)animated
{
    if (self.firstTime)
        self.firstTime = NO;
    else
        [[WSManager sharedInstance] getSpecificQuestions];
    
    [self updateEntireView];
}


- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.navBarMenuOptionButton.hidden = YES;
    self.navBarMenuOptionButton.enabled = NO;
}

- (void) updateCurrentQuestionIndex
{
    //show the first non answered question or the last questions
    //if all have already been answered
    if (self.lastAnsweredQuestion == self.questions.count)
        self.currentQuestionIndex = self.lastAnsweredQuestion - 1;
    else
        self.currentQuestionIndex = self.lastAnsweredQuestion;
}


- (void) updateEntireView
{
    Language lang = [BLController sharedInstance].language;
    self.navBarTitle.title = (lang == ENG) ? @"Questions" : @"Preguntas" ;
    self.questionLabel.text = (lang == ENG) ? ((DataQuestion*)self.questions[self.currentQuestionIndex]).textEng : ((DataQuestion*)self.questions[self.currentQuestionIndex]).textSpa;
    NSString *navBarMenuOptionButtonTitle = (lang == ENG) ? @"Reset questions" : @"Reiniciar preguntas";
    [self.navBarMenuOptionButton setTitle:navBarMenuOptionButtonTitle forState:UIControlStateNormal];
    
    [self.tableView reloadData]; //updates answers
    //[self adjustHeightOfTableview]; //changes tableView height to fit the content size
    [self updateNavigationControls]; //updates bottom arrows status (hidden, enabled)
}


- (void) adjustHeightOfTableview
{
    CGFloat height = self.tableView.contentSize.height;
    CGFloat maxHeight = self.tableView.superview.frame.size.height - self.tableView.frame.origin.y;
    
    if (height > maxHeight)
        height = maxHeight;
    
    [UIView animateWithDuration:0.25 animations:^{
        self.tableViewHeightConstraint.constant = height;
        [self.view setNeedsUpdateConstraints];
    }];
}

- (void) updateNavigationControls
{
    self.nextQuestionButton.hidden = (self.currentQuestionIndex == self.questions.count - 1);
    self.confirmAnswersButton.hidden = !self.nextQuestionButton.hidden || (self.lastConfirmedAnswer == self.questions.count);
    self.confirmAnswersLabel.hidden = self.confirmAnswersButton.hidden;
    self.previousQuestionButton.hidden = (self.currentQuestionIndex == 0);
    self.previousQuestionButton.enabled = YES;
    
    self.nextQuestionButton.enabled = (self.lastAnsweredQuestion > self.currentQuestionIndex);
    self.confirmAnswersButton.enabled = self.lastAnsweredQuestion == self.questions.count;
    self.confirmAnswersLabel.enabled = self.confirmAnswersButton.enabled;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


#pragma mark - Actions

- (IBAction)navBarMenuButtonPressed:(id)sender
{
    self.navBarMenuOptionButton.hidden = !self.navBarMenuOptionButton.hidden;
    self.navBarMenuOptionButton.enabled = !self.navBarMenuOptionButton.enabled;
}


- (IBAction)resetQuestionsButtonPressed:(id)sender
{
    self.navBarMenuOptionButton.hidden = YES;
    self.navBarMenuOptionButton.enabled = NO;
    
    Language lang = [BLController sharedInstance].language;
    NSString *title = (lang == ENG) ? @"Message" : @"Mensaje";
    NSString *cancel = (lang == ENG) ? @"Cancel" : @"Cancelar";
    NSString *ok = (lang == ENG) ? @"OK" : @"Aceptar";
    NSString *msg = (lang == ENG) ? @"Are you sure you want to reset the questions?" : @"¿Seguro que desea resetear las preguntas?";
    
    UIAlertView * alert =[[UIAlertView alloc ] initWithTitle:title
                                                     message:msg
                                                    delegate:self
                                           cancelButtonTitle:cancel
                                           otherButtonTitles: nil];
    [alert addButtonWithTitle:ok];
    [alert show];
}


- (IBAction)nextQuestionButtonPressed:(id)sender
{
    self.currentQuestionIndex++;
    [self updateEntireView];
}

- (IBAction)previousQuestionButtonPressed:(id)sender
{
    self.currentQuestionIndex--;
    [self updateEntireView];
}

- (IBAction)confirmAnswersButtonPressed:(id)sender
{
    BOOL ok = YES;
    self.confirmAnswersButton.enabled = NO;
    NSString *answers = [self getAnswersForWS];
    WSResponse *response;
    
    if (self.lastConfirmedAnswer == 0)
        response = [[WSManager sharedInstance] setGeneralAnswersSynchronous:answers];
    else
        response = [[WSManager sharedInstance] setSpecificAnswersSynchronous:answers];
    
    if (response.error == nil)
    {
        WSResponse *answeredQuestionsResponse = [[WSManager sharedInstance] getAnswersSynchronous];
        if (answeredQuestionsResponse.error == nil)
        {
            NSArray *answeredQuestions = answeredQuestionsResponse.data;
            
            WSResponse *specificQuestionsResponse = [[WSManager sharedInstance] getSpecificQuestionsSynchronous];
            if (specificQuestionsResponse.error == nil)
            {
                NSArray *specificQuestions = specificQuestionsResponse.data;
                self.lastConfirmedAnswer = answeredQuestions.count;
                self.questions = [[answeredQuestions arrayByAddingObjectsFromArray: specificQuestions] mutableCopy];
                [self updateCurrentQuestionIndex];
                [self updateEntireView];
            }
            else
            {
                ok = NO;
            }
        }
        else
        {
            ok = NO;
        }
    }
    else
    {
        ok = NO;
    }
    
    if (!ok)
    {
        [[BLController sharedInstance] showConnectionErrorAlert];
        self.confirmAnswersButton.enabled = YES;
    }
        
}



- (NSString *) getAnswersForWS
{
    NSRange range = NSMakeRange(MAX(self.lastConfirmedAnswer, 0), self.lastAnsweredQuestion - self.lastConfirmedAnswer);
    NSArray *answeredQuestions = [self.questions subarrayWithRange:range];
    NSMutableString *res = [[NSMutableString alloc]init];
    
    for (int i = 0; i < answeredQuestions.count; i++)
    {
        DataQuestion *q = answeredQuestions[i];
    
        if (i == answeredQuestions.count - 1)
            [res appendString:[NSString stringWithFormat:@"%ld", (long)q.selectedAnswer]];
        else
            [res appendString:[NSString stringWithFormat:@"%ld-", (long)q.selectedAnswer]];
    }
    
    return [res copy];
}


- (void)resetQuestions
{
    BOOL ok = YES;
    
    WSResponse *resetQuestionsResponse = [[WSManager sharedInstance] resetQuestionsSynchronous];
    
    if (resetQuestionsResponse.error == nil)
    {
        WSResponse *generalQuestionsResponse = [[WSManager sharedInstance] getGeneralQuestionsSynchronous];
        
        if (resetQuestionsResponse.error == nil)
        {
            self.questions = [generalQuestionsResponse.data mutableCopy];
            self.lastConfirmedAnswer = 0;
            self.currentQuestionIndex = 0;
            self.lastAnsweredQuestion = 0;
            [self updateEntireView];
        }
        else
        {
            ok = NO;
        }
    }
    else
    {
        ok = NO;
    }
    
    if (!ok)
    {
        [[BLController sharedInstance] showConnectionErrorAlert];
    }
    
}

#pragma mark - WSManager delegate

- (void)questionsResponseReceived:(NSArray *)questions
{
    BOOL add;
    
    for (DataQuestion *q in questions)
    {
        add = YES;
        
        for (DataQuestion *q2 in self.questions)
        {
            if (q.questionId == q2.questionId)
            {
                add = NO;
                break; //alreay got this question
            }
        }
        
        if (add)
            [self.questions addObject:q];
    }
    
}


#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1)
    {
        [self resetQuestions];
    }
}


#pragma mark - UITableViewDelegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[self.questions[self.currentQuestionIndex] answers] count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    MPSQuestionsTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIden];
    if (cell == nil)
        cell = [[MPSQuestionsTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIden];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    DataQuestion *question = self.questions[self.currentQuestionIndex];
    DataAnswer *answer = [question.answers  objectAtIndex:indexPath.row];
    
    if ([BLController sharedInstance].language == ENG)
        cell.answerLabel.text = answer.textEng;
    else
        cell.answerLabel.text = answer.textSpa;
    
    if ((question.selectedAnswer >= 0) && (question.selectedAnswer == answer.answerId))//already answered
    {
        cell.answerLabel.textColor = BTN_TITLE_COLOR_SELECTED;
        cell.answerLabel.backgroundColor = BTN_BG_COLOR_SELECTED;
    }
    else
    {
        cell.answerLabel.textColor = BTN_TITLE_COLOR_NORMAL;
        cell.answerLabel.backgroundColor = BTN_BG_COLOR_NORMAL;
    }
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //if the answer hasn't been already sent to server
    if (self.lastConfirmedAnswer <= self.currentQuestionIndex)
    {
        DataQuestion *question = self.questions[self.currentQuestionIndex];
        DataAnswer *answer = question.answers[indexPath.row];
        
        //if current selection is different from previous
        if (question.selectedAnswer != answer.answerId)
        {
            //if user has already answered this question
            if (question.selectedAnswer >= 0)
            {
                //find previously selected answer cell label and change its color to normal
                UILabel *lastAnswerLabel;
                
                for (int i = 0; i < question.answers.count; i++)
                {
                    if (question.selectedAnswer == [question.answers[i] answerId])
                    {
                        lastAnswerLabel = [[tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:0]] answerLabel];
                        break;
                    }
                }
                
                lastAnswerLabel.textColor = BTN_TITLE_COLOR_NORMAL;
                lastAnswerLabel.backgroundColor = BTN_BG_COLOR_NORMAL;
            }
            else
            {
                self.lastAnsweredQuestion++;
            }
            
            question.selectedAnswer = answer.answerId;
            [self updateEntireView];
        }
    }
}



@end
