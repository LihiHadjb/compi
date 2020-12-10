@.BS_vtable = global [6 x i8*] [
	i8* bitcast (i32 (i8*, i32)* @BS.Start to i8*),
	i8* bitcast (i1 (i8*, i32)* @BS.Search to i8*),
	i8* bitcast (i32 (i8*, i32)* @BS.Div to i8*),
	i8* bitcast (i1 (i8*, i32, i32)* @BS.Compare to i8*),
	i8* bitcast (i32 (i8*)* @BS.Print to i8*),
	i8* bitcast (i32 (i8*, i32)* @BS.Init to i8*)
]

