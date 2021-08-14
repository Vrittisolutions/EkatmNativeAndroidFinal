precision mediump float;
varying highp vec4 xlv_TEXCOORD0;
void main ()
{
  highp vec2 x_1;
  x_1 = (vec2(0.5, 0.5) - abs((xlv_TEXCOORD0.xy - vec2(0.5, 0.5))));
  bvec2 tmpvar_2;
  tmpvar_2 = lessThan (x_1, vec2(0.0, 0.0));
  if (any(tmpvar_2)) {
    discard;
  };
  highp vec4 result_3;
  highp vec2 tmpvar_4;
  tmpvar_4 = fract((vec2(1.0, 255.0) * xlv_TEXCOORD0.x));
  result_3.xy = (tmpvar_4 - (tmpvar_4.yx * vec2(0.003921569, 0.003921569)));
  highp vec2 tmpvar_5;
  tmpvar_5 = fract((vec2(1.0, 255.0) * xlv_TEXCOORD0.y));
  result_3.zw = (tmpvar_5 - (tmpvar_5.yx * vec2(0.003921569, 0.003921569)));
  gl_FragData[0] = result_3;
}

