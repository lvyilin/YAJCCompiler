int main() {
    //该用例覆盖了词法分析器中每一分支。注释包含了该语句中首次出现的类别编码
	int a=8,b=6,c;  //0 1 11 31 32
	char ch = 'x';  //55 34
	double d = 3.0; //2
	c = a+b;        //6
	c = a%2;         //10
	b += a;         //26
	a<<1;           //24
	if(a<b&&c){     //12 41 42 43 44 61 18
	    a++;        //22
	    ++a;        //22
	}
	else if(c!=b){ //16 62 21
	    a=a|b;      //19
	}
	else;
	char str[5];    //45 46
	str = "foo";    //31
	/*
	    This is a block comment
	*/
}