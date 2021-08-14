precision mediump float;
uniform highp vec4 SelectionIndex;
varying highp vec4 xlv_COLOR2;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1 = (xlv_COLOR2 + SelectionIndex);
  gl_FragData[0] = tmpvar_1;
}

